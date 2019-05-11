package com.pergamon.Pergamnon.v1.dao;

import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.Resource;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;

@Repository
public class ResourceDao {
    private EntityManager entityManager;

    @Autowired
    public ResourceDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(File file, URL url) throws IOException {
        Session session = entityManager.unwrap(Session.class);

        Resource resource = new Resource();
        resource.setUrl(url.getPath());
        resource.setFile(file);

        session.save(resource);
    }
}
