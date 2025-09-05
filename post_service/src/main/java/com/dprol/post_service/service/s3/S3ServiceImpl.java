package com.dprol.post_service.service.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.dprol.post_service.config.AmazonS3Config;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@PropertySource(ignoreResourceNotFound = true, value = "classpath:s3.properties")
@Slf4j

public class S3ServiceImpl implements S3Service{
    private final AmazonS3 s3Client;

    private final AmazonS3Config amazonS3Config;

    @Override
    public void uploadFile(MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        try {
            s3Client.putObject(amazonS3Config.getBucketName(), amazonS3Config.getSecretKey(), file.getInputStream(), metadata);
        } catch (SdkClientException | IOException e) {
            log.error("Error uploading a file to Amazon S3", e);
            throw new RuntimeException("Error uploading a file to Amazon S3", e);
        }
    }

    @Override
    public S3Object getFile(String bucketName, String key){
        S3Object s3Object;
        try {
            s3Object = s3Client.getObject(bucketName, key);
        } catch (SdkClientException e) {
            log.error("Error getting a file from Amazon S3", e);
            throw new RuntimeException("Error getting a file from Amazon S3", e);
        }
        return s3Object;
    }

    @Override
    public void deleteFile(String bucketName, String key){
        try {
            s3Client.deleteObject(bucketName, key);
        } catch (SdkClientException e) {
            log.error("Error deleting a file from Amazon S3", e);
            throw new RuntimeException("Error deleting a file from Amazon S3", e);
        }
    }

    @Override
    public InputStream downloadFile(String bucketName, String key) {
        return s3Client.getObject(bucketName, key).getObjectContent();
    }
}