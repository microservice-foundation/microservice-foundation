package com.epam.training.microservicefoundation.resourceservice.domain;

public class ResourceRecord {
    private final long id;

    public ResourceRecord(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
