package com.pergamon.Pergamon.v1.resource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceBodyTests {
    private ResourceBody resourceBody;

    @BeforeEach
    public void setUp() {
        resourceBody = new ResourceBody();
    }

    @Test
    public void testUrl() {
        String url = "https://example.com";
        resourceBody.setUrl(url);

        assertEquals(url, resourceBody.getUrl());
    }

    @Test
    public void testFileName() {
        resourceBody.setFileName("file.txt");

        assertEquals("file.txt", resourceBody.getFileName());
    }

    @Test
    public void testFileType() {
        resourceBody.setFileType("plain/text");

        assertEquals("plain/text", resourceBody.getFileType());
    }

    @Test
    public void testCreatedAt() {
        LocalDateTime date = LocalDateTime.now();
        resourceBody.setCreatedAt(date);

        assertEquals(date, resourceBody.getCreatedAt());
    }
}
