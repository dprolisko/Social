package com.dprol.social.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserProfileDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.entity.user.UserProfile;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.user.UserProfileMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.service.amazonS3.S3Service;
import com.dprol.social.service.user.UserService;
import com.dprol.social.service.user.avatar.AvatarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceImplTest {
    @InjectMocks
    private AvatarServiceImpl profilePicService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Service s3Service;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private UserProfileMapper pictureMapper = Mappers.getMapper(UserProfileMapper.class);
    @Captor
    private ArgumentCaptor<String> forPictures;
    private User user;
    private UserDto userDto;
    private final String backetName = "user-bucket";

    private byte[] getImageBytes() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.RED);
        graphics.fill(new Rectangle2D.Double(0, 0, 50, 50));
        byte[] bytes;
        try {
            ImageIO.write(image, "jpg", outputStream);
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @BeforeEach
    void init() {
        user = User.builder().id(1L).username("name").email("test@mail.ru").password("password").userProfile(new UserProfile("Big picture", "Small picture")).build();
        userDto = UserDto.builder().id(1L).username("name").email("test@mail.ru").password("password").build();
    }

    @Test
    public void testGenerateAndSetPicWithException(){
        when(restTemplate.getForObject(anyString(),eq(byte[].class))).thenReturn(null);
        var exception = assertThrows(DataValidationException.class, ()->profilePicService.generatedPicture(userDto));
        assertEquals(exception.getMessage(), "failed to generate picture");
    }

    @Test
    public void testGenerateAndSetPicWithSetting(){
        when(restTemplate.getForObject(anyString(),eq(byte[].class))).thenReturn(getImageBytes());
        doNothing().when(s3Service).uploadFile(anyString(), anyString(), any(InputStream.class));
        ReflectionTestUtils.setField(profilePicService, "smallSize", 170);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);

        profilePicService.generatedPicture(userDto);
        UserProfile generated = userDto.getUserprofile();
        assertNull(generated.getFileId());
        assertNotNull(generated.getSmallFileId());

        InOrder inorder = inOrder(restTemplate, s3Service);
        inorder.verify(restTemplate, times(1)).getForObject(anyString(),eq(byte[].class));
        inorder.verify(s3Service, times(1)).uploadFile(anyString(), anyString(), any(InputStream.class));
    }

    @Test
    public void testSaveProfilePicWithSaving() {
        when(userService.findUserById(user.getId())).thenReturn(user);
        MockMultipartFile file = new MockMultipartFile("file", "example.jpg", MediaType.IMAGE_JPEG_VALUE, getImageBytes());
        ReflectionTestUtils.setField(profilePicService, "smallSize", 170);
        ReflectionTestUtils.setField(profilePicService, "largeSize", 1080);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);

        UserProfileDto result = profilePicService.saveProfilePicture(user.getId(), file);

        verify(userService, times(1)).findUserById(user.getId());
        verify(s3Service, times(2)).uploadFile(eq(backetName), forPictures.capture(), any(InputStream.class));
        var pictures = forPictures.getAllValues();
        String forSmallPicture = pictures.get(0);
        String forLargePicture = pictures.get(1);
        verify(userRepository, times(1)).save(user);
        verify(pictureMapper, times(1)).toDto(UserProfile.builder().smallFileId(forLargePicture).fileId(forSmallPicture).build());
        assertEquals(result.getFileId(), forSmallPicture);
        assertEquals(result.getSmallFileId(), forLargePicture);
    }@Test
    public void testGetProfilePic() {
        when(userService.findUserById(user.getId())).thenReturn(user);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new ByteArrayInputStream(getImageBytes()));
        when(s3Service.getFile(backetName, user.getUserProfile().getFileId())).thenReturn(s3Object);

        InputStreamResource result = profilePicService.getProfilePicture(user.getId());
        verify(userService, times(1)).findUserById(user.getId());
        verify(s3Service, times(1)).getFile(backetName, user.getUserProfile().getFileId());
        assertNotNull(result);
    }

    @Test
    public void testDeleteProfilePic() {
        when(userService.findUserById(user.getId())).thenReturn(user);
        ReflectionTestUtils.setField(profilePicService, "bucketName", backetName);

        profilePicService.deleteProfilePicture(user.getId());
        verify(userService, times(1)).findUserById(user.getId());
        verify(s3Service, times(2)).deleteFile(eq(backetName), anyString());
        verify(userRepository, times(1)).save(user);
        assertNull(user.getUserProfile().getFileId());
        assertNull(user.getUserProfile().getSmallFileId());
    }
}