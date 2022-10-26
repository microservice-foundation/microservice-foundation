package com.epam.training.microservicefoundation.resourceprocessor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class ResourceConvertor implements Convertor<File, ResponseInputStream<GetObjectResponse>> {
    private static final Logger log = LoggerFactory.getLogger(ResourceConvertor.class);
    private static final String FILE_TYPE = "mp3";
    @Override
    public File covert(ResponseInputStream<GetObjectResponse> inputStream) throws IOException {
        log.info("Converting bytes with length '{}' to file", inputStream.response().contentLength());
        File directory = ResourceUtils.getFile("classpath:temp");
        File newFile = new File(directory, inputStream.response().eTag() + "." + FILE_TYPE);
        try(FileOutputStream fileOutputStream =
                    new FileOutputStream(newFile)) {
            fileOutputStream.write(inputStream.readAllBytes());
        } catch (IOException ex) {
            IllegalStateException illegalStateException = new IllegalStateException(
                    String.format("Resource file '%1s' conversion failed:", newFile.getName()), ex);

            log.error("Resource file '{}' conversion failed: ", newFile.getName(), illegalStateException);
            throw illegalStateException;
        }
        return newFile;
    }
}
