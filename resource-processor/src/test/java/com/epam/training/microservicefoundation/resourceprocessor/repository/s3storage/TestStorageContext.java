package com.epam.training.microservicefoundation.resourceprocessor.repository.s3storage;

import com.epam.training.microservicefoundation.resourceprocessor.configuration.AwsS3Configuration;
import com.epam.training.microservicefoundation.resourceprocessor.repository.CloudStorageRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.s3.S3Client;

@TestConfiguration
public class TestStorageContext {

    @Bean
    CloudStorageRepository cloudStorageRepository(AwsS3Configuration configuration, S3Client s3Client) {
        return new CloudStorageRepository(configuration, s3Client);
    }
}
