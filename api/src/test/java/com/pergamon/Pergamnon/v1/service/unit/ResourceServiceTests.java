package com.pergamon.Pergamnon.v1.service.unit;

import com.pergamon.Pergamnon.v1.dao.FileDao;
import com.pergamon.Pergamnon.v1.dao.ResourceDao;
import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamnon.v1.entity.Resource;
import com.pergamon.Pergamnon.v1.service.FileStorageService;
import com.pergamon.Pergamnon.v1.service.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResourceService.class)
public class ResourceServiceTests {
    private FilePropertiesPojo fileProperties;

    @Autowired
    private ResourceService resourceService;

    @Mock
    private URL url;

    @Mock
    private File file;

    @Mock
    private org.springframework.core.io.Resource fileResource;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private FileDao fileDao;

    @MockBean
    private ResourceDao resourceDao;

    @Before
    public void setUp() {
        fileProperties = new FilePropertiesPojo();

        when(file.getName()).thenReturn("file.txt");
        when(file.getStorageName()).thenReturn(UUID.randomUUID().toString());
        when(file.getType()).thenReturn("plain/text");
    }

    @Test
    public void testCreate_WhenCorrectData() throws Exception {
        when(fileStorageService.storeFile(any(URL.class))).thenReturn(fileProperties);
        when(fileDao.save(fileProperties)).thenReturn(file);

        resourceService.create(url);

        verify(resourceDao, times(1)).save(file, url);
    }

    @Test
    public void testList_WhenNoResources() throws Exception {
        List<Resource> resources = getFilledResourceList();
        when(resourceDao.list()).thenReturn(resources);

        Assert.assertSame(resources, resourceService.list());
    }

    @Test
    public void testList_WhenNoResourcesWithSearch() throws Exception {
        List<Resource> resources = new ArrayList<>();
        when(resourceDao.list("www")).thenReturn(resources);

        Assert.assertSame(resources, resourceService.list("www"));
    }

    @Test
    public void testList_WhenWithResourceWithSearch() throws Exception {
        List<Resource> resources = getFilledResourceList();
        when(resourceDao.list("www")).thenReturn(resources);

        Assert.assertSame(resources, resourceService.list("www"));
    }

    @Test
    public void testDownload() {
        when(fileDao.findByUrl(any(URL.class))).thenReturn(file);
        when(fileStorageService.loadFileAsResource(any(String.class))).thenReturn(fileResource);

        resourceService.download(url);
    }

    @Test
    public void testUpdate_WhenCorrectData() throws Exception {
        when(fileDao.findByUrl(url)).thenReturn(file);

        resourceService.update(url);

        verify(fileStorageService, atLeastOnce()).updateFile(url, file);
    }

    private List<Resource> getFilledResourceList() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource());

        return resources;
    }
}
