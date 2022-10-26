package com.epam.training.microservicefoundation.resourceservice.domain;

public class FileStorageResponse {
    private final String path;
    private final String fileName;

    public FileStorageResponse(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }
}
