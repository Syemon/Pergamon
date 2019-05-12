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
        this.resourceBody = new ResourceBody();
    }

    @Test
    public void testUrl() {
        String url = "https://example.com";
        this.resourceBody.setUrl(url);

        Assert.assertEquals(url, this.resourceBody.getUrl());
    }

    @Test
    public void testFileName() {
        this.resourceBody.setFileName("file.txt");

        Assert.assertEquals("file.txt", this.resourceBody.getFileName());
    }

    @Test
    public void testFileType() {
        this.resourceBody.setFileType("plain/text");

        Assert.assertEquals("plain/text", this.resourceBody.getFileType());
    }

    @Test
    public void testCreatedAt() {
        Date date = new Date();
        this.resourceBody.setCreatedAt(date);

        Assert.assertEquals(date, this.resourceBody.getCreatedAt());
    }
}
