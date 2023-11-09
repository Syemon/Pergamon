package com.pergamon.Pergamon.v1.dao.integration;

import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.dataaccess.FileEntity;
import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import com.pergamon.Pergamon.v1.service.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
@ActiveProfiles(value = Profile.TEST_FLYWAY)
public class PostgresResourceRepositoryTests extends PostgresTestContainerResourceTest {

    private static final String URL = "https://example.com";

    @Autowired
    private PostgresFileRepository postgresFileRepository;

    @Autowired
    private PostgresResourceRepository sut;

    private URL url;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        url = new URL(URL);
    }

    @Test
    @Transactional
    public void testCreate_WhenCorrectData() throws MalformedURLException {
        sut.save(getFile(), new URL("https://testcreate.qwerty"));
    }

    @Test
    @Transactional
    public void create() throws MalformedURLException {
        // given
        URL url = new URL("https://newurl.xyz");
        ResourceCommand command = new ResourceCommand().setUrl(url);

        // when
        ResourceEntity result = sut.create(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotZero();
        assertThat(result.getFileId()).isNull();
        assertThat(result.getStatus()).isEqualTo(ResourceStatus.NEW);
        assertThat(result.getUrl()).isEqualTo(url.toString());
        assertThat(result.getCreatedAt()).isCloseTo(OffsetDateTime.now(), within(30L, ChronoUnit.SECONDS));
        assertThat(result.getModifiedAt()).isNull();
    }

    @Test
    @Transactional
    public void testExists_WhenNotExists_ReturnFalse() throws MalformedURLException {
        assertThat(sut.exists(new URL("https://example2.com"))).isFalse();
    }

    @Test
    @Transactional
    public void testExists_WhenExists_ReturnTrue() {
        assertThat(sut.exists(url)).isTrue();
    }

    @Transactional
    public FileEntity getFile() {
        FileEntity file = FileEntity.builder()
                .name("test.txt")
                .storageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .type("plain/text")
                .build();

        return postgresFileRepository.save(file);
    }
}
