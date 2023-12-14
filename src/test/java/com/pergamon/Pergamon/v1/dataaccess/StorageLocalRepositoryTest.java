package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StorageLocalRepositoryTest {

    public static final String CONTENT_STORAGE_NAME = "5bf94590-bd2c-4593-92df-5b31d1b0bca1";
    public static final String FILE_NAME = "test.txt";
    static final String STORAGE_PATH = "./test_uploads";

    private Path path;
    @InjectMocks
    private StorageLocalRepository sut;

    private Resource resource;
    private Content content;

    @Mock
    private URL url;

    @BeforeEach
    public void setUp() {
        path =  Paths.get(STORAGE_PATH)
                .toAbsolutePath().normalize();
        sut = new StorageLocalRepository(path);

        resource = new Resource()
                .setStatus(ResourceStatus.NEW)
                .setUrl(url);

        content = Content.builder()
                .name(FILE_NAME)
                .storageName(CONTENT_STORAGE_NAME)
                .type("plain/text")
                .build();
    }

    @Test
    void store_shouldSetPositiveStatus_whenSuccessfullyStoredResource() throws IOException {
        //given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        when(url.openStream()).thenReturn(inputStream);

        //when
        Resource resultResource = sut.store(resource, content);

        //then
        Assertions.assertThat(resultResource.getStatus()).isEqualTo(ResourceStatus.DONE);
    }

    @Test
    void store_shouldSetNegativeStatus_whenCouldNotStoreResource() throws IOException {
        //given
        when(url.openStream()).thenThrow(new IOException());

        //when
        Resource resultResource = sut.store(resource, content);

        //then
        Assertions.assertThat(resultResource.getStatus()).isEqualTo(ResourceStatus.ERROR);
    }
}