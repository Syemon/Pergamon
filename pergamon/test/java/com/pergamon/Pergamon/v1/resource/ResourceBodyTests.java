package com.pergamon.Pergamon.v1.resource;

import com.pergamon.Pergamon.v1.service.ResourceBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

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
        OffsetDateTime date = OffsetDateTime.now();
        resourceBody.setCreatedAt(date);

        assertEquals(date, resourceBody.getCreatedAt());
    }
}
