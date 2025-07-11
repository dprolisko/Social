package com.dprol.social.service;

import com.dprol.social.service.amazonS3.S3ServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class S3ServiceImplTest {

    @Mock
    private AmazonS3 s3Client;

    @Mock
    private Logger log; // SLF4j logger

    @InjectMocks
    private S3ServiceImpl s3Service;

    // Тестовые данные
    private final String BUCKET = "test-bucket";
    private final String KEY = "test-key";
    private final InputStream INPUT_STREAM = new ByteArrayInputStream("test data".getBytes());
    private final S3Object s3Object = mock(S3Object.class);
    private final SdkClientException sdkException = new SdkClientException("AWS error");

    // ------------------------ uploadFile() ------------------------

    @Test
    void uploadFile_Success() {
        // Действие
        s3Service.uploadFile(BUCKET, KEY, INPUT_STREAM);

        // Проверка
        verify(s3Client).putObject(
                eq(BUCKET),
                eq(KEY),
                eq(INPUT_STREAM),
                any(ObjectMetadata.class)
        );
        verifyNoInteractions(log);
    }

    @Test
    void uploadFile_ThrowsException() {
        // Подготовка
        doThrow(sdkException).when(s3Client).putObject(any(), any(), any(), any());

        // Действие + Проверка
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> s3Service.uploadFile(BUCKET, KEY, INPUT_STREAM));

        assertEquals("Error uploading a file to Amazon S3", ex.getMessage());
        assertSame(sdkException, ex.getCause());
        verify(log).error("Error uploading a file to Amazon S3", sdkException);
    }

    @Test
    void uploadFile_NullInputStream() {
        // Действие + Проверка
        assertThrows(NullPointerException.class,
                () -> s3Service.uploadFile(BUCKET, KEY, null));
    }

    // ------------------------ getFile() ------------------------

    @Test
    void getFile_Success() {
        // Подготовка
        when(s3Client.getObject(BUCKET, KEY)).thenReturn(s3Object);

        // Действие
        S3Object result = s3Service.getFile(BUCKET, KEY);

        // Проверка
        assertSame(s3Object, result);
        verifyNoInteractions(log);
    }

    @Test
    void getFile_ThrowsException() {
        // Подготовка
        when(s3Client.getObject(BUCKET, KEY)).thenThrow(sdkException);

        // Действие + Проверка
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> s3Service.getFile(BUCKET, KEY));

        assertEquals("Error getting a file from Amazon S3", ex.getMessage());
        assertSame(sdkException, ex.getCause());
        verify(log).error("Error getting a file from Amazon S3", sdkException);
    }

    @Test
    void getFile_EmptyKey() {
        // Действие + Проверка
        assertThrows(IllegalArgumentException.class,
                () -> s3Service.getFile(BUCKET, ""));
    }

    // ------------------------ deleteFile() ------------------------

    @Test
    void deleteFile_Success() {
        // Действие
        s3Service.deleteFile(BUCKET, KEY);

        // Проверка
        verify(s3Client).deleteObject(BUCKET, KEY);
        verifyNoInteractions(log);
    }

    @Test
    void deleteFile_ThrowsException() {
        // Подготовка
        doThrow(sdkException).when(s3Client).deleteObject(BUCKET, KEY);

        // Действие + Проверка
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> s3Service.deleteFile(BUCKET, KEY));

        assertEquals("Error deleting a file from Amazon S3", ex.getMessage());
        assertSame(sdkException, ex.getCause());
        verify(log).error("Error deleting a file from Amazon S3", sdkException);
    }

    @Test
    void deleteFile_NullBucket() {
        // Действие + Проверка
        assertThrows(IllegalArgumentException.class,
                () -> s3Service.deleteFile(null, KEY));
    }

    // ------------------------ Edge Cases ------------------------

    @Test
    void uploadFile_ContentTypeCustomization() {
        // Действие
        s3Service.uploadFile(BUCKET, KEY, INPUT_STREAM);

        // Проверка метаданных
        ArgumentCaptor<ObjectMetadata> metadataCaptor = ArgumentCaptor.forClass(ObjectMetadata.class);
        verify(s3Client).putObject(eq(BUCKET), eq(KEY), eq(INPUT_STREAM), metadataCaptor.capture());

        ObjectMetadata metadata = metadataCaptor.getValue();
        assertEquals("image/jpeg", metadata.getContentType());
    }

    @Test
    void getFile_ReturnsNullObject() {
        // Подготовка
        when(s3Client.getObject(BUCKET, KEY)).thenReturn(null);

        // Действие
        S3Object result = s3Service.getFile(BUCKET, KEY);

        // Проверка
        assertNull(result);
    }
}