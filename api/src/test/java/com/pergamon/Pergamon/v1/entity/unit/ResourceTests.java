package com.pergamon.Pergamon.v1.entity.unit;

import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.Resource;
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
        resource  = new Resource();
    }

    @Test
    public void testUrl() {
        String url = "https://example.com";
        resource.setUrl(url);

        Assert.assertEquals(url, resource.getUrl());
    }

    @Test
    public void testFile() {
        resource.setFile(file);

        Assert.assertEquals(file, resource.getFile());
    }
}
