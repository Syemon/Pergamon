package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.ContentEntity;
import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URL;
import java.time.OffsetDateTime;
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
    public static final String CONTENT_TYPE = "plain/text";
    public static final String FILE_NAME = "file.txt";
    private ContentEntity contentEntity;
    private ResourceCommand resourceCommand;

    @InjectMocks
    private ResourceService sut;

    @Mock
    private URL url;

    @Mock
    private org.springframework.core.io.Resource fileResource;

    @Mock
    private ContentService contentService;

    @Mock
    private PostgresFileRepository fileDao;

    @Mock
    private PostgresResourceRepository resourceDao;

    @Mock
    private ResourceCollectionModelCreator resourceResourceCreator;

    @BeforeEach
    public void setUp() {
        contentEntity = ContentEntity.builder().build();
        contentEntity.setType(CONTENT_TYPE);
        contentEntity.setName(FILE_NAME);

        resourceCommand = new ResourceCommand().setUrl(url);
    }

    @Test
    public void testCreate_WhenCorrectData() {
        // given
        when(contentService.storeFile(any(URL.class))).thenReturn(contentEntity);
        when(fileDao.save(contentEntity)).thenReturn(contentEntity);

        Resource resource = new Resource().setUrl(url);

        // when
        sut.create(resource);

        // then
        verify(resourceDao, times(1)).save(contentEntity, url);
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
        when(fileDao.findByUrl(any(URL.class))).thenReturn(contentEntity);

        sut.download(url);
    }

    @Test
    public void testUpdate_WhenCorrectData() throws Exception {
        when(fileDao.findByUrl(url)).thenReturn(contentEntity);

        sut.update(url);

        verify(contentService, atLeastOnce()).updateFile(url, contentEntity);
    }

    private List<ResourceEntity> getFilledResourceList() {
        List<ResourceEntity> resources = new ArrayList<>();
        resources.add(new ResourceEntity()
                .setId(1)
                .setFileId(1)
                .setUrl("name")
                .setCreatedAt(OffsetDateTime.now()));

        return resources;
    }
}
