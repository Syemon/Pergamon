package com.pergamon.Pergamnon.v1.service.unit;

import com.pergamon.Pergamnon.PergamnonApplication;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PergamnonApplication.class)
@AutoConfigureMockMvc()
public class FileStorageServiceTests {
    @Mock
    private URL url;

    @Mock
    private URLConnection urlConnection;

    @Mock
    private FilenameUtils filenameUtils = new FilenameUtils();

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testStoreFile() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Mockito.when(url.getPath()).thenReturn("https://example/test.txt");
        Mockito.when(url.openStream()).thenReturn(inputStream);
        Mockito.when(url.openConnection()).thenReturn(this.urlConnection);
        Mockito.when(this.urlConnection.getContentType()).thenReturn("plain/text");

        FilePropertiesPojo fileProperties = fileStorageService.storeFile(this.url);

        Assert.assertEquals(fileProperties.getName(), "test.txt");
        Assert.assertTrue(fileProperties.getStorageName()
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
