package com.pergamon.Pergamnon.v1.resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
public class ResourceBodyTests {
    private ResourceBody resourceBody;

    @Before
    public void setUp() {
        resourceBody = new ResourceBody();
    }

    @Test
    public void testUrl() {
        String url = "https://example.com";
        resourceBody.setUrl(url);

        Assert.assertEquals(url, resourceBody.getUrl());
    }

    @Test
    public void testFileName() {
        resourceBody.setFileName("file.txt");

        Assert.assertEquals("file.txt", resourceBody.getFileName());
    }

    @Test
    public void testFileType() {
        resourceBody.setFileType("plain/text");

        Assert.assertEquals("plain/text", resourceBody.getFileType());
    }

    @Test
    public void testCreatedAt() {
        Date date = new Date();
        resourceBody.setCreatedAt(date);

        Assert.assertEquals(date, resourceBody.getCreatedAt());
    }
}
