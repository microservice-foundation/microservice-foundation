package com.epam.training.microservicefoundation.resourceservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
@Component
public class SongFileConvertor implements Convertor<File, MultipartFile> {
    private static final Logger log = LoggerFactory.getLogger(SongFileConvertor.class);
    @Override
    public File covert(MultipartFile input) {
        log.info("Converting multipart file '{}' to file", input.getName());
        File file = new File(Objects.requireNonNull(input.getOriginalFilename()));
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(input.getBytes());
        } catch (IOException ex) {
            IllegalStateException illegalStateException = new IllegalStateException(
                    String.format("Multipart file '%1s' conversion failed:", file.getName()), ex);

            log.error("Multipart file '{}' conversion failed: ", file.getName(), illegalStateException);
            throw illegalStateException;
        }
        return file;
    }
}
