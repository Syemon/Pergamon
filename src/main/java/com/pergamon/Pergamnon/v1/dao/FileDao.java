package com.pergamon.Pergamnon.v1.dao;

import com.pergamon.Pergamnon.v1.entity.File;
import com.pergamon.Pergamnon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamnon.v1.property.FileStorageProperties;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.io.IOException;

@Repository
public class FileDao {
    private EntityManager entityManager;
    private FileStorageProperties fileStorageProperties;

    @Autowired
    public FileDao(EntityManager entityManager, FileStorageProperties fileStorageProperties) {
        this.entityManager = entityManager;
        this.fileStorageProperties = fileStorageProperties;
    }

    public File save(FilePropertiesPojo filePropertiesPojo) throws IOException {
        Session session = entityManager.unwrap(Session.class);

        File file = new File();
        file.setName(filePropertiesPojo.getName());
        file.setStorageName(filePropertiesPojo.getStorageName());
        file.setType(filePropertiesPojo.getType());

        session.save(file);

        return file;
    }
}
