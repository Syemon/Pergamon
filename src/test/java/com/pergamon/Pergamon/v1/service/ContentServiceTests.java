package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.ContentEntity;
import org.apache.commons.io.FilenameUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContentServiceTests {
    @InjectMocks
    private ContentService sut = new ContentService("./test_uploads");

    @Mock
    private URL url;

    @Mock
    private URLConnection urlConnection;

    @Mock
    private FilenameUtils filenameUtils = new FilenameUtils();

    @Test
    void validateInitialContent_shouldReturnFailureObject_whenPathIsNotValid() {
        // given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        when(url.getPath()).thenReturn("https://example/..test.txt");

        // when
        Optional<ValidationError> result = sut.validateInitialContent(url);

        //then
        assertThat(result).isPresent();
        ValidationError validationError = result.get();
        assertThat(validationError.getFailureMessages()).hasSize(1);
        assertThat(validationError.getFailureMessages()).contains("Filename contains invalid path sequence");
    }

    @Test
    public void testStoreFile() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        when(url.getPath()).thenReturn("https://example/test.txt");
        when(url.openStream()).thenReturn(inputStream);
        when(url.openConnection()).thenReturn(urlConnection);
        when(urlConnection.getContentType()).thenReturn("plain/text");

        ContentEntity contentEntity = sut.storeFile(url);

        assertEquals(contentEntity.getName(), "test.txt");
        assertTrue(contentEntity.getStorageName()
                .matches("[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"));
    }
}
