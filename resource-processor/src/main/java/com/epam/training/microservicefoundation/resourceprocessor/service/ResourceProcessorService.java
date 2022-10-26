package com.epam.training.microservicefoundation.resourceprocessor.service;

import com.epam.training.microservicefoundation.resourceprocessor.domain.ResourceRecord;
import com.epam.training.microservicefoundation.resourceprocessor.domain.SongRecord;
import com.epam.training.microservicefoundation.resourceprocessor.repository.CloudStorageRepository;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;

@Service
public class ResourceProcessorService {
    private static final Logger log = LoggerFactory.getLogger(ResourceProcessorService.class);
    private final CloudStorageRepository repository;
    private final ResourceRecordValidator resourceRecordValidator;
    private final ResourceConvertor resourceConvertor;

    @Autowired
    public ResourceProcessorService(CloudStorageRepository repository,
                                    ResourceRecordValidator resourceRecordValidator,
                                    ResourceConvertor resourceConvertor) {
        this.repository = repository;
        this.resourceRecordValidator = resourceRecordValidator;
        this.resourceConvertor = resourceConvertor;
    }


    public SongRecord getResourceData(ResourceRecord resourceRecord) throws InvalidDataException, UnsupportedTagException, IOException {
        log.info("Getting a song metadata by a resource record '{}'", resourceRecord);
        if(!resourceRecordValidator.validate(resourceRecord)) {
            IllegalArgumentException ex = new IllegalArgumentException(String.format("Resource record '%s' " +
                    "was not validated, check your required parameters", resourceRecord));

            log.error("Resource record '{}' was not valid to parse metadata\nreason:", resourceRecord, ex);
            throw ex;
        }

        ResponseInputStream<GetObjectResponse> songData = repository.getByName(resourceRecord.getName());
        Mp3File mp3File = new Mp3File(resourceConvertor.covert(songData));
        return new SongRecord.Builder(resourceRecord.getId(), resourceRecord.getName(),
                        String.format(
                "%1d:%2d", mp3File.getLengthInSeconds()/60, mp3File.getLengthInSeconds()%60))
                .artist(mp3File.getId3v1Tag().getArtist())
                .album(mp3File.getId3v1Tag().getAlbum())
                .year(Integer.parseInt(mp3File.getId3v1Tag().getYear()))
                .build();
    }

}
