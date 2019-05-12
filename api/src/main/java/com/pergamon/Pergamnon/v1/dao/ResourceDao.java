package com.pergamon.Pergamnon.v1.dao;

import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.Resource;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

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
        resource.setUrl(url.toString());
        resource.setFile(file);

        session.save(resource);
    }

    public boolean exists(URL url) {
        Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery(
                "SELECT 1 " +
                        "FROM Resource " +
                        "WHERE url=:url");

        return query.setParameter("url", url.toString())
                .uniqueResult() != null;
    }

    public List<Resource> list() {
        Session session = entityManager.unwrap(Session.class);

        Query<Resource> query =
                session.createQuery("FROM Resource", Resource.class);

        return query.getResultList();
    }
}
