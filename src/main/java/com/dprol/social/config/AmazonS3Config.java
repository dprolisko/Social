package com.dprol.social.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${services.s3.accesskey}")
    private String accessKey;

    @Value("${services.s3.secretkey}")
    private String secretKey;

    @Value("${services.s3.bucketname}")
    private String bucketName;

    @Value("${services.s3.endpoint}")
    private String endPoint;

    @Value("${services.s3.targetwidth}")
    private int targetWidth;

    @Value("${services.s3.targetheight}")
    private int targetHeight;

    @Value("${services.s3.maxfilesize}")
    private int maxFileSize;

    @Bean
    public AmazonS3 amazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, null))
                .withPathStyleAccessEnabled(true).build();
    }
}
