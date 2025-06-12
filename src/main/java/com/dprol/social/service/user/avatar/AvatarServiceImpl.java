package com.dprol.social.service.user.avatar;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserProfileDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.entity.user.UserProfile;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.mapper.user.UserProfileMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.service.amazonS3.S3Service;
import com.dprol.social.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:s3.properties")

public class AvatarServiceImpl implements AvatarService {

    private final UserRepository userRepository;

    private final UserService userService;

    private final UserMapper userMapper;

    private final UserProfileMapper userProfileMapper;

    private final S3Service s3Service;

    @Value("${randomAvatar.url}")
    private String url;

    @Value("${smallSize}")
    private int smallSize;

    @Value("${largeSize}")
    private int largeSize;

    @Value("${bucketName}")
    private String bucketName;

    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public UserProfileDto saveProfilePicture(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        String nameSmallPicture = "small" + file.getName() + LocalDateTime.now();
        String nameLargePicture = "large" + file.getName() + LocalDateTime.now();
        try{
            s3Service.uploadFile(bucketName, nameSmallPicture, compressor(file.getInputStream(), smallSize));
            s3Service.uploadFile(bucketName, nameLargePicture, compressor(file.getInputStream(), largeSize));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserProfile userProfile = new UserProfile(nameSmallPicture, nameLargePicture);
        user.setUserProfile(userProfile);
        userRepository.save(user);
        return userProfileMapper.toDto(userProfile);
    }

    @Override
    @Transactional
    public UserProfileDto deleteProfilePicture(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        s3Service.deleteFile(bucketName, user.getUserProfile().getFileId());
        s3Service.deleteFile(bucketName, user.getUserProfile().getSmallFileId());
        UserProfileDto userProfileDto = userProfileMapper.toDto(user.getUserProfile());
        user.getUserProfile().setSmallFileId(null);
        user.getUserProfile().setFileId(null);
        userRepository.save(user);
        return userProfileDto;
    }

    @Override
    @Transactional
    public InputStreamResource getProfilePicture(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        S3Object s3Object = s3Service.getFile(bucketName, user.getUserProfile().getFileId());
        return new InputStreamResource(s3Object.getObjectContent());
    }

    @Override
    @Retryable(retryFor = {RestClientException.class}, maxAttempts = 5, backoff = @Backoff(delay = 5000, multiplier = 2))
    @Transactional
    public void generatedPicture(UserDto userDto) {
        byte[] byteArray = restTemplate.getForObject(url + userDto.getUsername(), byte[].class);
        if (byteArray == null) {
            throw new DataValidationException("failed to generate picture");
        }
        String nameSmallPicture = "small" + userDto.getUsername() + LocalDateTime.now() + ".jpg";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("image/jpg");
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        s3Service.uploadFile(bucketName, nameSmallPicture, compressor(inputStream, smallSize));
        UserProfile userProfile = new UserProfile();
        userProfile.setSmallFileId(nameSmallPicture);
        userDto.setUserprofile(userProfile);
    }

    private InputStream compressor(InputStream inputStream, int size) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            BufferedImage bufferedImage = Thumbnails.of(inputStream).size(size, size).asBufferedImage();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
