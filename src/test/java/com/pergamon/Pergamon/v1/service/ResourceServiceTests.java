package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.domain.FileEntity;
import com.pergamon.Pergamon.v1.domain.FileId;
import com.pergamon.Pergamon.v1.domain.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ResourceService.class)
public class ResourceServiceTests {
    private FileEntity fileEntity;

    @Autowired
    private ResourceService sut;

    @Mock
    private URL url;

    @Mock
    private FileEntity file;

    @Mock
    private org.springframework.core.io.Resource fileResource;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private PostgresFileRepository fileDao;

    @MockBean
    private PostgresResourceRepository resourceDao;

    @MockBean
    private ResourceCollectionModelCreator resourceResourceCreator;

    @BeforeEach
    public void setUp() {
        fileEntity = FileEntity.builder().build();

        when(file.getName()).thenReturn("file.txt");
        when(file.getStorageName()).thenReturn(UUID.randomUUID().toString());
        when(file.getType()).thenReturn("plain/text");
    }

    @Test
    public void testCreate_WhenCorrectData() throws Exception {
        when(fileStorageService.storeFile(any(URL.class))).thenReturn(fileEntity);
        when(fileDao.save(fileEntity)).thenReturn(file);

        sut.create(url);

        verify(resourceDao, times(1)).save(file, url);
    }

    @Test
    public void testList_WhenNoResources() throws Exception {
        List<ResourceEntity> resources = getFilledResourceList();
        when(resourceDao.list()).thenReturn(resources);

        assertSame(resources, sut.list());
    }

    @Test
    public void testList_WhenNoResourcesWithSearch() throws Exception {
        List<ResourceEntity> resources = new ArrayList<>();
        when(resourceDao.list("www")).thenReturn(resources);

        assertSame(resources, sut.list("www"));
    }

    @Test
    public void testList_WhenWithResourceWithSearch() throws Exception {
        List<ResourceEntity> resources = getFilledResourceList();
        when(resourceDao.list("www")).thenReturn(resources);

        assertSame(resources, sut.list("www"));
    }

    @Test
    public void testDownload() {
        when(fileDao.findByUrl(any(URL.class))).thenReturn(file);
        when(fileStorageService.loadFileAsResource(any(String.class))).thenReturn(fileResource);

        sut.download(url);
    }

    @Test
    public void testUpdate_WhenCorrectData() throws Exception {
        when(fileDao.findByUrl(url)).thenReturn(file);

        sut.update(url);

        verify(fileStorageService, atLeastOnce()).updateFile(url, file);
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
