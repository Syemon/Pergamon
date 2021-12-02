package com.pergamon.Pergamon.v1.dao.integration;

import com.pergamon.Pergamon.PergamonApplication;
import com.pergamon.Pergamon.v1.dao.FileDao;
import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.entity.Resource;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@Disabled
@SpringBootTest(classes = PergamonApplication.class)
public class FileDaoTests {
    private FilePropertiesPojo fileProperties;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileDao fileDao;

    @BeforeEach
    public void setUp() {
        fileProperties = new FilePropertiesPojo();
    }

    @Test
    @Transactional
    public void testCreate_WhenCorrectData_ReturnFile() {
        fileProperties
                .setName("test.txt")
                .setStorageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .setType("plain/text");

        Session session = entityManager.unwrap(Session.class);

        File file = fileDao.save(fileProperties);
        session.getTransaction().commit();
        session.refresh(file);

        assertSame("test.txt", file.getName());
        assertSame("8d4073ce-17d5-43e1-90a0-62e94fba1402", file.getStorageName());
        assertSame("plain/text", file.getType());
        assertNotNull(file.getCreatedAt());
        assertNotNull(file.getId());
    }

    @Test
    @Transactional
    public void testFindByUrl_WhenExists_ReturnFile() throws IOException {
        Resource resource = getResource();
        URL url = new URL("https://example.com");

        Session session = entityManager.unwrap(Session.class);

        File file = fileDao.findByUrl(url);
        
        session.getTransaction().commit();
        session.refresh(file);

        assertEquals(resource.getFile(), file);
    }

    @Test
    @Transactional
    public void testUpdate() {
        File file = getFile();

        file.setName("new_name");
        file.setStorageName("lorem ipsum");
        file.setType("plain/html");
        Session session = entityManager.unwrap(Session.class);

        fileDao.update(file);
        session.getTransaction().commit();
        session.refresh(file);

        assertEquals(file.getName(), "new_name");
        assertEquals(file.getStorageName(), "lorem ipsum");
        assertEquals(file.getType(), "plain/html");
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
}
