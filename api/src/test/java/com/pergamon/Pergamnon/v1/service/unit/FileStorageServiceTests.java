package com.pergamon.Pergamnon.v1.service.unit;

import com.pergamon.Pergamnon.PergamnonApplication;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamnon.v1.service.FileStorageService;
import org.apache.commons.io.FilenameUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PergamnonApplication.class)
@AutoConfigureMockMvc()
public class FileStorageServiceTests {
    @Autowired
    private FileStorageService fileStorageService;

    @Mock
    private URL url;

    @Mock
    private URLConnection urlConnection;

    @Mock
    private FilenameUtils filenameUtils = new FilenameUtils();

    @Test
    public void testStoreFile() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        when(url.getPath()).thenReturn("https://example/test.txt");
        when(url.openStream()).thenReturn(inputStream);
        when(url.openConnection()).thenReturn(this.urlConnection);
        when(this.urlConnection.getContentType()).thenReturn("plain/text");

        FilePropertiesPojo fileProperties = fileStorageService.storeFile(this.url);

        Assert.assertEquals(fileProperties.getName(), "test.txt");
        Assert.assertTrue(fileProperties.getStorageName()
                .matches("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"));
    }
}
