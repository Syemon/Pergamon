package com.pergamon.Pergamnon.v1.dao.integration;

import com.pergamon.Pergamnon.PergamnonApplication;
import com.pergamon.Pergamnon.v1.dao.FileDao;
import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PergamnonApplication.class)
public class FileDaoTests {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private FileDao fileDao;

    FilePropertiesPojo fileProperties;

    @Before
    public void setUp() {
        this.fileProperties = new FilePropertiesPojo();
    }

    @Test
    @Transactional
    public void testCreate_WhenCorrectData_ReturnFile() throws IOException {
        String storageName = UUID.randomUUID().toString();
        this.fileProperties
                .setName("test.txt")
                .setStorageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .setType("plain/text");

        Session session = entityManager.unwrap(Session.class);

        File file = fileDao.save(this.fileProperties);
        session.flush();
        session.refresh(file);

        Assert.assertSame("test.txt", file.getName());
        Assert.assertSame("8d4073ce-17d5-43e1-90a0-62e94fba1402", file.getStorageName());
        Assert.assertSame("plain/text", file.getType());
        Assert.assertNotNull(file.getCreatedAt());
        Assert.assertNotNull(file.getId());
    }
}
