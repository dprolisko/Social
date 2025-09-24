package com.dprol.post_service.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.dprol.post_service.config.AmazonS3Config;
import com.dprol.post_service.service.s3.S3ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class S3ServiceImplTest {

    @Mock
    private AmazonS3 s3Client;
    @Mock
    private AmazonS3Config amazonS3Config;
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private S3ServiceImpl s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(amazonS3Config.getBucketName()).thenReturn("bucket-test");
        when(amazonS3Config.getSecretKey()).thenReturn("secret-key");
    }

    @Test
    void uploadFile_ShouldPutObject() throws IOException {
        when(multipartFile.getSize()).thenReturn(10L);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        s3Service.uploadFile(multipartFile);

        verify(s3Client).putObject(eq("bucket-test"), eq("secret-key"), any(InputStream.class), any(ObjectMetadata.class));
    }

    @Test
    void uploadFile_ShouldThrowRuntimeException_WhenSdkClientException() throws IOException {
        when(multipartFile.getSize()).thenReturn(10L);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));
        doThrow(new SdkClientException("fail")).when(s3Client)
                .putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> s3Service.uploadFile(multipartFile));
        assertTrue(ex.getMessage().contains("Error uploading a file"));
    }

    @Test
    void uploadFile_ShouldThrowRuntimeException_WhenIOException() throws IOException {
        when(multipartFile.getSize()).thenReturn(10L);
        when(multipartFile.getInputStream()).thenThrow(new IOException("IO fail"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> s3Service.uploadFile(multipartFile));
        assertTrue(ex.getMessage().contains("Error uploading a file"));
    }

    @Test
    void getFile_ShouldReturnS3Object() {
        S3Object s3Object = new S3Object();
        when(s3Client.getObject("bucket-test", "key1")).thenReturn(s3Object);

        S3Object result = s3Service.getFile("bucket-test", "key1");

        assertSame(s3Object, result);
    }

    @Test
    void getFile_ShouldThrowRuntimeException_WhenSdkClientException() {
        when(s3Client.getObject("bucket-test", "key1")).thenThrow(new SdkClientException("fail"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> s3Service.getFile("bucket-test", "key1"));
        assertTrue(ex.getMessage().contains("Error getting a file"));
    }

    @Test
    void deleteFile_ShouldCallDeleteObject() {
        s3Service.deleteFile("bucket-test", "key1");

        verify(s3Client).deleteObject("bucket-test", "key1");
    }

    @Test
    void deleteFile_ShouldThrowRuntimeException_WhenSdkClientException() {
        doThrow(new SdkClientException("fail")).when(s3Client).deleteObject("bucket-test", "key1");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> s3Service.deleteFile("bucket-test", "key1"));
        assertTrue(ex.getMessage().contains("Error deleting a file"));
    }

    @Test
    void downloadFile_ShouldReturnInputStream() {
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new ByteArrayInputStream("data".getBytes()));
        when(s3Client.getObject("bucket-test", "key1")).thenReturn(s3Object);

        InputStream result = s3Service.downloadFile("bucket-test", "key1");

        assertNotNull(result);
    }
}
