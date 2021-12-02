package com.pergamon.Pergamon.v1.entity.unit;

import com.pergamon.Pergamon.v1.entity.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileTests {
    private File file;

    @BeforeEach
    public void setUp() {
        file  = new File();
    }

    @Test
    public void testName() {
        String name = "Lorem";
        file.setName(name);

        assertEquals(name, file.getName());
    }

    @Test
    public void testStorageName() {
        String storageName = UUID.randomUUID().toString();
        file.setStorageName(storageName);

        assertEquals(storageName, file.getStorageName());
    }

    @Test
    public void testType() {
        String type = "image/jpeg";
        file.setType(type);

        assertEquals(type, file.getType());
    }
}
