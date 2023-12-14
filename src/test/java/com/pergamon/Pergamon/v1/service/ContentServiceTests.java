package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.ContentCommand;
import com.pergamon.Pergamon.v1.domain.ContentDomainException;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ContentServiceTests {
    public static final String UUID_PATTERN = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";
    public static final String FILE_NAME = "test.txt";
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
        when(url.getPath()).thenReturn("https://wikipedia.com/..test.txt");

        // when
        Optional<ValidationError> result = sut.validateInitialContent(url);

        //then
        assertThat(result).isPresent();
        ValidationError validationError = result.get();
        assertThat(validationError.getFailureMessages()).hasSize(1);
        assertThat(validationError.getFailureMessages()).contains("Filename contains invalid path sequence");
    }



    @Test
    public void createContentCommand_shouldThrowException_whenCouldNotExtractContentType() throws Exception {
        // given
        when(url.getPath()).thenReturn("https://example/test.txt");
        when(url.openConnection()).thenThrow(new IOException("Test exception"));

        // when/then
        Assertions.assertThatThrownBy(() -> sut.createContentCommand(url))
                .isExactlyInstanceOf(ContentDomainException.class)
                .hasMessage("Problem while saving content");
    }

    @Test
    public void createContentCommand() throws Exception {
        // given
        when(url.getPath()).thenReturn("https://example/test.txt");
        when(url.openConnection()).thenReturn(urlConnection);
        when(urlConnection.getContentType()).thenReturn("plain/text");

        // when
        ContentCommand result = sut.createContentCommand(url);

        // then
        assertThat(result.getType()).isEqualTo("plain/text");
        assertThat(result.getStorageName()).isNotNull();
        assertThat(result.getStorageName()).matches(UUID_PATTERN);
        assertThat(result.getName()).isEqualTo(FILE_NAME);
    }
}
