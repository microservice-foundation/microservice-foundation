package com.epam.training.microservicefoundation.songservice.domain;

public class SongRecordId {
    private final long id;

    public SongRecordId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
