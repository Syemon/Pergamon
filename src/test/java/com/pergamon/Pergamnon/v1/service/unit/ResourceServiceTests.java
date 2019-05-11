package com.pergamon.Pergamnon.v1.service.unit;

import com.pergamon.Pergamnon.v1.dao.FileDao;
import com.pergamon.Pergamnon.v1.dao.ResourceDao;
import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamnon.v1.entity.Resource;
import com.pergamon.Pergamnon.v1.exception.ResourceCreationException;
import com.pergamon.Pergamnon.v1.service.FileStorageService;
import com.pergamon.Pergamnon.v1.service.ResourceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ResourceService.class)
public class ResourceServiceTests {
    @Mock
    private URL url;

    @Mock
    private File file;

    @MockBean
    private FileStorageService fileStorageService;

    @MockBean
    private FileDao fileDao;

    @MockBean
    private ResourceDao resourceDao;

    @Autowired
    private ResourceService resourceService;

    private FilePropertiesPojo fileProperties;

    @Before
    public void setUp() {
        this.fileProperties = new FilePropertiesPojo();
    }

    @Test
    public void testCreate_WhenCorrectData() throws Exception {
        Mockito.when(fileStorageService.storeFile(Mockito.any(URL.class))).thenReturn(this.fileProperties);
        Mockito.when(fileDao.save(this.fileProperties)).thenReturn(this.file);

        this.resourceService.create(this.url);

        verify(this.resourceDao, times(1)).save(this.file, this.url);
    }

    @Test(expected = ResourceCreationException.class)
    public void testCreate_WhenThrowedIOException_ReturnException() throws Exception {
        Mockito.when(fileStorageService.storeFile(Mockito.any(URL.class))).thenReturn(this.fileProperties);
        Mockito.when(fileDao.save(this.fileProperties)).thenThrow(IOException.class);

        this.resourceService.create(this.url);
    }

    @Test
    public void testList_WhenNoResources() throws Exception {
        List<Resource> resources = this.getFilledResourceList();
        Mockito.when(resourceDao.list()).thenReturn(resources);

        Assert.assertSame(resources, this.resourceService.list());
    }

    private List<Resource> getFilledResourceList() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource());

        return resources;
    }
}
