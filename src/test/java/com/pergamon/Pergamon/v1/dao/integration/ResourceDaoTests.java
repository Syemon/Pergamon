package com.pergamon.Pergamon.v1.dao.integration;

import com.pergamon.Pergamon.PergamonApplication;
import com.pergamon.Pergamon.v1.dao.FileDao;
import com.pergamon.Pergamon.v1.dao.ResourceDao;
import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.entity.Resource;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ResourceDaoTests {
    private Resource resource;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileDao fileDao;

    @Autowired
    private ResourceDao resourceDao;

    @Mock
    private URL url;

    @BeforeEach
    public void setUp() {
        resource = new Resource();
    }

    @Test
    @Transactional
    public void testCreate_WhenCorrectData() {
        Session session = entityManager.unwrap(Session.class);

        resourceDao.save(getFile(), url);
    }

    @Disabled("rewrite with docker db")
    @Test
    @Transactional
    public void testList() {
        Resource resource = getResource();

        List<Resource> resources = resourceDao.list();

        assertSame(resource, resources.get(0));
    }

    @Disabled("rewrite with docker db")
    @Test
    @Transactional
    public void testList_WhenSearchingForNotExistentUrl_ReturnEmptyList() {
        Resource resource = getNamedResourceAndFile("https://example.com", "test.txt");

        List<Resource> resources = resourceDao.list("search");

        assertFalse(resources.contains(resource));
    }

    @Test
    @Disabled("rewrite with docker db")
    @Transactional
    public void testList_WhenSearchingForExistentUrl_ReturnList() {
        Resource resource = getNamedResourceAndFile("https://example.com", "test.txt");

        List<Resource> resources = resourceDao.list("example");

        assertSame(resource, resources.get(0));
    }

    @Disabled("rewrite with docker db")
    @Test
    @Transactional
    public void testList_WhenSearchingForExistentUrlWithDifferentCase_ReturnList() {
        Resource resource = getNamedResourceAndFile("https://example.com", "test.txt");

        List<Resource> resources = resourceDao.list("eXample");

        assertSame(resource, resources.get(0));
    }

    @Test
    @Transactional
    public void testExists_WhenNotExists_ReturnFalse() {
        Mockito.when(url.toString()).thenReturn("https://example2.com");

        Resource resource = getResource();

        assertFalse(resourceDao.exists(url));
    }

    @Test
    @Transactional
    public void testExists_WhenExists_ReturnTrue() {
        Mockito.when(url.toString()).thenReturn("https://example.com");

        Resource resource = getResource();

        assertTrue(resourceDao.exists(url));
    }

    @Transactional
    public File getFile() {
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
    public Resource getResource() {
        Resource resource = new Resource();

        Session session = entityManager.unwrap(Session.class);

        resource.setUrl("https://example.com");
        resource.setFile(getFile());

        session.save(resource);
        session.flush();
        session.refresh(resource);

        return resource;
    }

    @Transactional
    public Resource getNamedResourceAndFile(String url, String fileName) {
        File file = new File();
        Resource resource = new Resource();

        file.setName(fileName);
        file.setStorageName("8d4073ce-17d5-43e1-90a0-62e94fba1402");
        file.setType("plain/text");

        resource.setUrl(url);
        resource.setFile(file);

        Session session = entityManager.unwrap(Session.class);

        session.save(file);
        session.save(resource);
        session.flush();
        session.refresh(resource);

        return resource;
    }
}
