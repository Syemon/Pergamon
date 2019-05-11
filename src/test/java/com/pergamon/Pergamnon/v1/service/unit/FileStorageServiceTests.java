package com.pergamon.Pergamnon.v1.service.unit;

import com.pergamon.Pergamnon.PergamnonApplication;
import com.pergamon.Pergamnon.v1.service.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PergamnonApplication.class)
@AutoConfigureMockMvc()
public class FileStorageServiceTests {
    @Mock
    URL url;

    @Mock
    FilenameUtils filenameUtils = new FilenameUtils();

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testStoreFile() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Mockito.when(url.getPath()).thenReturn("https://example/test.txt");
        Mockito.when(url.openStream()).thenReturn(inputStream);

        Map<String, String> storedFileNames = fileStorageService.storeFile(this.url);

        Assert.assertEquals(storedFileNames.get("fileName"), "test.txt");
        Assert.assertTrue(storedFileNames.get("storageFileName")
                .matches("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"));
    }

    @Test
    public void testDeleteFile() throws Exception {
        Files.createFile(Paths.get("test_uploads/testDelete.txt"));

        fileStorageService.deleteFile("testDelete.txt");

        boolean exists = Files.exists(Paths.get("test_uploads/testDelete.txt"));
        Assert.assertFalse(exists);
    }
}
