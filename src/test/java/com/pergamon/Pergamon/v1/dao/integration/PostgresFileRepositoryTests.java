package com.pergamon.Pergamon.v1.dao.integration;

import com.pergamon.Pergamon.PergamonApplication;
import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.dataaccess.ContentEntity;
import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.service.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = PergamonApplication.class)
@ActiveProfiles(value = Profile.TEST_FLYWAY)
public class PostgresFileRepositoryTests extends PostgresTestContainerResourceTest {
    private ContentEntity contentEntity;

    @Autowired
    private PostgresFileRepository fileDao;

    @Autowired
    private PostgresResourceRepository postgresResourceRepository;

    @BeforeEach
    public void setUp() {
        contentEntity = ContentEntity.builder().build();
    }

    @Test
    public void testCreate_WhenCorrectData_ReturnFile() {
        contentEntity = ContentEntity.builder()
                .name("test.txt")
                .storageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .type("plain/text")
                .build();


        ContentEntity file = fileDao.save(contentEntity);

        assertThat(file.getName()).isEqualTo("test.txt");
        assertThat(file.getStorageName()).isEqualTo("8d4073ce-17d5-43e1-90a0-62e94fba1402");
        assertThat(file.getType()).isEqualTo("plain/text");
        assertThat(file.getCreatedAt()).isNotNull();
        assertThat(file.getId()).isNotNull();
        assertThat(file.getUpdatedAt()).isNull();
    }

    @Test
    public void testFindByUrl_WhenExists_ReturnFile() throws MalformedURLException {
        URL url = new URL("https://example.com");
        ResourceEntity resource = postgresResourceRepository.findByUrl(url.toString()).get();


        ContentEntity file = fileDao.findByUrl(url);

        assertThat(file.getId()).isEqualTo(resource.getFileId());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdate() { //FIXME: It can end up as a false positive
        ContentEntity file = getFile();

        file.setName("new_name");
        file.setStorageName("lorem ipsum");
        file.setType("plain/html");

        fileDao.update(file);

        assertThat(file.getName()).isEqualTo("new_name");
        assertThat(file.getStorageName()).isEqualTo("lorem ipsum");
        assertThat(file.getType()).isEqualTo("plain/html");
    }

    public ContentEntity getFile() {
        ContentEntity file = ContentEntity.builder()
                .name("test.txt")
                .storageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .type("plain/text")
                .build();

        return fileDao.save(file);
    }
}
