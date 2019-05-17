package com.pergamon.Pergamon.v1.dao;

import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.property.FileStorageProperties;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.net.URL;

@Repository
public class FileDao {
    private EntityManager entityManager;
    private FileStorageProperties fileStorageProperties;

    @Autowired
    public FileDao(EntityManager entityManager, FileStorageProperties fileStorageProperties) {
        this.entityManager = entityManager;
        this.fileStorageProperties = fileStorageProperties;
    }

    public File save(FilePropertiesPojo filePropertiesPojo) {
        Session session = entityManager.unwrap(Session.class);

        File file = new File();
        file.setName(filePropertiesPojo.getName());
        file.setStorageName(filePropertiesPojo.getStorageName());
        file.setType(filePropertiesPojo.getType());

        session.save(file);

        return file;
    }

    public void update(File file) {
        Session session = entityManager.unwrap(Session.class);

        session.save(file);
    }

    public File findByUrl(URL url) {
        Session session = entityManager.unwrap(Session.class);

        Query<File> query =
                session.createQuery("SELECT f " +
                        "FROM Resource AS r " +
                        "INNER JOIN File AS f " +
                        "ON r.file = f " +
                        "WHERE r.url = :url", File.class);

        return query.setParameter("url", url.toString()).getSingleResult();
    }
}
