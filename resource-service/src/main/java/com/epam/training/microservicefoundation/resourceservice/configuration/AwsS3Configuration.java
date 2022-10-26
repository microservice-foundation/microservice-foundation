package com.epam.training.microservicefoundation.resourceservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AwsS3Configuration {

    @Value("${aws.s3.endpoint}")
    private String amazonS3Endpoint;
    @Value("${aws.s3.bucket-name}")
    private String amazonS3BucketName;

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .credentialsProvider(getProfileCredentialsProvider())
                .endpointOverride(URI.create(amazonS3Endpoint))
                .region(Region.US_EAST_1)
                .build();
    }



    private ProfileCredentialsProvider getProfileCredentialsProvider() {
        return ProfileCredentialsProvider.create("administrator");
    }

    public String getAmazonS3Endpoint() {
        return amazonS3Endpoint;
    }

    public String getAmazonS3BucketName() {
        return amazonS3BucketName;
    }
}
