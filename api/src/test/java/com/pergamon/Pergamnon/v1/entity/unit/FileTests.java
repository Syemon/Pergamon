package com.pergamon.Pergamnon.v1.entity.unit;

import com.pergamon.Pergamnon.v1.entity.File;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
public class FileTests {
    private File file;

    @Before
    public void setUp() {
        this.file  = new File();
    }

    @Test
    public void testName() {
        String name = "Lorem";
        this.file.setName(name);

        Assert.assertEquals(name, this.file.getName());
    }

    @Test
    public void testStorageName() {
        String storageName = UUID.randomUUID().toString();
        this.file.setStorageName(storageName);

        Assert.assertEquals(storageName, this.file.getStorageName());
    }

    @Test
    public void testType() {
        String type = "image/jpeg";
        this.file.setType(type);

        Assert.assertEquals(type, file.getType());
    }
}