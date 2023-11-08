package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.dataaccess.FileEntity;
import com.pergamon.Pergamon.v1.domain.FileId;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTests {
    private FileEntity fileEntity;
    private ResourceCommand resourceCommand;

    @InjectMocks
    private ResourceService sut;

    @Mock
    private URL url;

    @Mock
    private org.springframework.core.io.Resource fileResource;

    @Mock
    private FileService fileService;

    @Mock
    private PostgresFileRepository fileDao;

    @Mock
    private PostgresResourceRepository resourceDao;

    @Mock
    private ResourceCollectionModelCreator resourceResourceCreator;

    @BeforeEach
    public void setUp() {
        fileEntity = FileEntity.builder().build();
        fileEntity.setType("plain/text");
        fileEntity.setName("file.txt");

        resourceCommand = new ResourceCommand().setUrl(url);
    }

    @Test
    public void testCreate_WhenCorrectData() {
        when(fileService.storeFile(any(URL.class))).thenReturn(fileEntity);
        when(fileDao.save(fileEntity)).thenReturn(fileEntity);

        sut.create(resourceCommand);

        verify(resourceDao, times(1)).save(fileEntity, url);
    }

    @Test
    public void testList_WhenNoResources() {
        List<ResourceEntity> resources = getFilledResourceList();
        when(resourceDao.list()).thenReturn(resources);

        assertSame(resources, sut.list());
    }

    @Test
    public void testList_WhenNoResourcesWithSearch() {
        List<ResourceEntity> resources = new ArrayList<>();
        when(resourceDao.list("www")).thenReturn(resources);

        assertSame(resources, sut.list("www"));
    }

    @Test
    public void testList_WhenWithResourceWithSearch() {
        List<ResourceEntity> resources = getFilledResourceList();
        when(resourceDao.list("www")).thenReturn(resources);

        assertSame(resources, sut.list("www"));
    }

    @Test
    public void testDownload() {
        when(fileDao.findByUrl(any(URL.class))).thenReturn(fileEntity);

        sut.download(url);
    }

    @Test
    public void testUpdate_WhenCorrectData() throws Exception {
        when(fileDao.findByUrl(url)).thenReturn(fileEntity);

        sut.update(url);

        verify(fileService, atLeastOnce()).updateFile(url, fileEntity);
    }

    private List<ResourceEntity> getFilledResourceList() {
        List<ResourceEntity> resources = new ArrayList<>();
        resources.add(new ResourceEntity()
                .setId(new ResourceId(1))
                .setFileId(new FileId(1))
                .setUrl("name")
                .setCreatedAt(LocalDateTime.now()));

        return resources;
    }
}
