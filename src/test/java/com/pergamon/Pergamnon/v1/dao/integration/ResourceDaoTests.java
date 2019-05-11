package com.pergamon.Pergamnon.v1.dao.integration;

import com.pergamon.Pergamnon.PergamnonApplication;
import com.pergamon.Pergamnon.v1.dao.FileDao;
import com.pergamon.Pergamnon.v1.dao.ResourceDao;
import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamnon.v1.entity.Resource;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PergamnonApplication.class)
public class ResourceDaoTests {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private ResourceDao resourceDao;

    @Mock
    private URL url;

    private Resource resource;

    @Before
    public void setUp() {
        this.resource = new Resource();
    }

    @Test
    @Transactional
    public void testCreate_WhenCorrectData() throws IOException {
        Mockito.when(url.getPath()).thenReturn("https://example/test.txt");

        Session session = entityManager.unwrap(Session.class);

        resourceDao.save(this.getFile(), this.url);
    }

    @Test
    @Transactional
    public void testList() throws IOException {
        Session session = entityManager.unwrap(Session.class);
        Resource resource = this.getResource();

        List<Resource> resources = resourceDao.list();

        Assert.assertSame(resource, resources.get(0));
    }

    @Transactional
    public File getFile() throws IOException {
        FilePropertiesPojo fileProperties = new FilePropertiesPojo();
        fileProperties
                .setName("test.txt")
                .setStorageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .setType("plain/text");

        Session session = entityManager.unwrap(Session.class);

        File file = fileDao.save(fileProperties);
        session.flush();
        session.refresh(file);

        return file;
    }

    @Transactional
    public Resource getResource() throws IOException {
        Resource resource = new Resource();

        Session session = entityManager.unwrap(Session.class);

        resource.setUrl("https://example.com");
        resource.setFile(this.getFile());

        session.save(resource);
        session.flush();
        session.refresh(resource);

        return resource;
    }
}
