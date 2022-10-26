package com.epam.training.microservicefoundation.resourceservice.service.convertor;

import com.epam.training.microservicefoundation.resourceservice.service.SongFileConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class SongFileConvertorTest {
    private SongFileConvertor convertor;

    @BeforeEach
    void setup() {
        convertor = new SongFileConvertor();
    }


    @Test
    void shouldConvert() throws IOException {
        File songFile = ResourceUtils.getFile("classpath:files/mpthreetest.mp3");

        MultipartFile multipartFile = new MockMultipartFile(songFile.getName(), songFile.getName(),
                "audio/mpeg", Files.readAllBytes(songFile.toPath()));

        File convertedFile = convertor.covert(multipartFile);
        assertNotNull(convertedFile);
        assertEquals(songFile.length(), convertedFile.length());
        assertEquals(songFile.getName(), convertedFile.getName());
    }


}