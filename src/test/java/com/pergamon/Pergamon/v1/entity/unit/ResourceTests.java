package com.pergamon.Pergamon.v1.entity.unit;

import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceTests {
    private Resource resource;

    @Mock
    private File file;

    @BeforeEach
    public void setUp() {
        resource  = new Resource();
    }

    @Test
    public void testUrl() {
        String url = "https://example.com";
        resource.setUrl(url);

        assertEquals(url, resource.getUrl());
    }

    @Test
    public void testFile() {
        resource.setFile(file);

        assertEquals(file, resource.getFile());
    }
}
