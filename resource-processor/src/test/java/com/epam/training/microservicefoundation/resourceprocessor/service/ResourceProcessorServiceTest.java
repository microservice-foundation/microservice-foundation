package com.epam.training.microservicefoundation.resourceprocessor.service;

import com.epam.training.microservicefoundation.resourceprocessor.domain.ResourceRecord;
import com.epam.training.microservicefoundation.resourceprocessor.domain.SongRecord;
import com.epam.training.microservicefoundation.resourceprocessor.repository.CloudStorageRepository;
import com.epam.training.microservicefoundation.resourceprocessor.repository.s3storage.CloudStorageExtension;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(CloudStorageExtension.class)
class ResourceProcessorServiceTest {

    @Autowired
    private CloudStorageRepository repository;
    @Autowired
    private ResourceProcessorService service;


    @Test
    void shouldGetResourceMetadata() throws InvalidDataException, UnsupportedTagException, IOException {
        // upload a file
        File file = ResourceUtils.getFile("classpath:files/mpthreetest.mp3");
        repository.upload(file);

        ResourceRecord resourceRecord = new ResourceRecord(1L, file.getName(), file.getPath());
        SongRecord result = service.getResourceData(resourceRecord);
        assertNotNull(result);
        assertEquals(resourceRecord.getId(), result.getResourceId());
        assertEquals(resourceRecord.getName(), result.getName());
        assertNotNull(result.getLength());
    }

    @Test
    void shouldThrowValidationExceptionWhenGetResourceMetadataWithInvalidResourceId() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:files/mpthreetest.mp3");
        assertThrows(IllegalArgumentException.class, () -> service.getResourceData(new ResourceRecord(0L,
                file.getName(), file.getPath())));
    }

    @Test
    void shouldThrowValidationExceptionWhenGetResourceMetadataWithNullName() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:files/mpthreetest.mp3");
        assertThrows(IllegalArgumentException.class, () -> service.getResourceData(new ResourceRecord(32L,
                null, file.getPath())));
    }

    @Test
    void shouldThrowValidationExceptionWhenGetResourceMetadataWithNullPath() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:files/mpthreetest.mp3");
        assertThrows(IllegalArgumentException.class, () -> service.getResourceData(new ResourceRecord(23L,
                file.getName(), null)));
    }
}