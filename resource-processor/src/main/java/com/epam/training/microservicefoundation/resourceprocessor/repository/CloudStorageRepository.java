package com.epam.training.microservicefoundation.resourceprocessor.repository;

import com.epam.training.microservicefoundation.resourceprocessor.configuration.AwsS3Configuration;
import com.epam.training.microservicefoundation.resourceprocessor.domain.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.util.Objects;

@Repository
public class CloudStorageRepository {

    private static final Logger log = LoggerFactory.getLogger(CloudStorageRepository.class);
    private final AwsS3Configuration configuration;
    private final S3Client s3Client;

    @Autowired
    public CloudStorageRepository(AwsS3Configuration configuration, S3Client s3Client) {
        this.configuration = configuration;
        this.s3Client = s3Client;
    }

    public String upload(File file) {
        log.info("Uploading file '{}' to bucket '{}'", file.getName(), configuration);

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(configuration.getAmazonS3BucketName())
                .key(file.getName())
                .build();

        PutObjectResponse putObjectResponse = s3Client.putObject(objectRequest, RequestBody.fromFile(file));
        if(Objects.nonNull(putObjectResponse)) {
            log.debug("File '{}' uploaded successfully to bucket '{}': response: {}", file.getName(),
                    configuration.getAmazonS3BucketName(), putObjectResponse);

            return configuration.getAmazonS3Endpoint() + "/" +configuration.getAmazonS3BucketName() + "/" + file.getName();
        }

        IllegalStateException ex = new IllegalStateException(String.format("File '%1s' upload failed to bucket '%2s'",
                file.getName(), configuration.getAmazonS3BucketName()));

        log.error("Uploading file failed:", ex);
        throw ex;
    }

    public ResponseInputStream<GetObjectResponse> getByName(String name) {
        log.info("Getting song by name '{}' from bucket '{}'", name, configuration.getAmazonS3BucketName());
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(configuration.getAmazonS3BucketName())
                .key(name)
                .build();
        try {
            return s3Client.getObject(request);
        } catch (NoSuchKeyException ex) {
            ResourceNotFoundException exception = new ResourceNotFoundException(String.format("Resource was not found with " +
                    "key %1s", name), ex);

            log.error("Resource not found with key '{}' \nreason: ", name, ex);
            throw exception;
        }
    }

}
