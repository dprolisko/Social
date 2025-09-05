package com.dprol.post_service.service.s3;

import com.amazonaws.services.s3.model.S3Object;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface S3Service {

    void uploadFile(MultipartFile file);

    S3Object getFile(String bucketName, String key);

    void deleteFile(String bucketName, String key);

    InputStream downloadFile(String bucketName, String key);
}
