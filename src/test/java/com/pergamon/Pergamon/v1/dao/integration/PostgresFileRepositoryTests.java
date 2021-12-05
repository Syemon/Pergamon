package com.pergamon.Pergamon.v1.dao.integration;

import com.pergamon.Pergamon.PergamonApplication;
import com.pergamon.Pergamon.v1.dao.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dao.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.entity.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PergamonApplication.class)
public class PostgresFileRepositoryTests {
    private FilePropertiesPojo fileProperties;

    @Autowired
    private PostgresFileRepository fileDao;

    @Autowired
    private PostgresResourceRepository postgresResourceRepository;

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


        File file = fileDao.save(fileProperties);

        assertThat(file.getName()).isEqualTo("test.txt");
        assertThat(file.getStorageName()).isEqualTo("8d4073ce-17d5-43e1-90a0-62e94fba1402");
        assertThat(file.getType()).isEqualTo("plain/text");
        assertThat(file.getCreatedAt()).isNotNull();
        assertThat(file.getId()).isNotNull();
        assertThat(file.getUpdatedAt()).isNull();
    }

    @Test
    @Transactional
    public void testFindByUrl_WhenExists_ReturnFile() throws MalformedURLException {
        Resource resource = getResource();
        URL url = new URL("https://example.com");

        File file = fileDao.findByUrl(url);

        assertThat(file.getId()).isEqualTo(resource.getFileId());
    }

    @Test
    @Transactional
    public void testUpdate() { //FIXME: It can end up as a false positive
        File file = getFile();

        file.setName("new_name");
        file.setStorageName("lorem ipsum");
        file.setType("plain/html");

        fileDao.update(file);

        assertThat(file.getName()).isEqualTo("new_name");
        assertThat(file.getStorageName()).isEqualTo("lorem ipsum");
        assertThat(file.getType()).isEqualTo("plain/html");
    }

    @Transactional
    public File getFile() {
        FilePropertiesPojo fileProperties = new FilePropertiesPojo();
        fileProperties
                .setName("test.txt")
                .setStorageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .setType("plain/text");

        return fileDao.save(fileProperties);
    }

    @Transactional
    public Resource getResource() throws MalformedURLException {
        return postgresResourceRepository.save(getFile(), new URL("https://example.com"));
    }
}
