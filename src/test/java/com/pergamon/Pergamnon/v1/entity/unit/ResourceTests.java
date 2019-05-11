package com.pergamon.Pergamnon.v1.entity.unit;

import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ResourceTests {
    private Resource resource;

    @Mock
    private File file;

    @Before
    public void setUp() {
        this.resource  = new Resource();
    }

    @Test
    public void testUrl() {
        String url = "https://example.com";
        this.resource.setUrl(url);

        Assert.assertEquals(url, this.resource.getUrl());
    }

    @Test
    public void testFile() {
        this.resource.setFile(this.file);

        Assert.assertEquals(this.file, this.resource.getFile());
    }
}
