package com.pergamon.Pergamon.v1.dao.integration;

import com.pergamon.Pergamon.v1.dao.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dao.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.entity.File;
import com.pergamon.Pergamon.v1.entity.FilePropertiesPojo;
import com.pergamon.Pergamon.v1.entity.Resource;
import org.assertj.core.data.TemporalUnitOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
public class PostgresResourceRepositoryTests {

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
    public void testCreate_WhenCorrectData() {
        sut.save(getFile(), url);
    }

    @Test
    @Transactional
    public void testList() {
        Resource resource = getResource();

        List<Resource> resources = sut.list();

        assertThat(resources.get(0).getId()).isEqualTo(resource.getId());
        assertThat(resources.get(0).getFileId()).isEqualTo(resource.getFileId());
        assertThat(resources.get(0).getUrl()).isEqualTo(resource.getUrl());
    }

    @Test
    @Transactional
    public void testList_WhenSearchingForNotExistentUrl_ReturnEmptyList() {
        Resource resource = getResource();

        List<Resource> resources = sut.list("search");

        assertThat(resources).doesNotContain(resource);
    }

    @Test
    @Transactional
    public void testList_WhenSearchingForExistentUrl_ReturnList() {
        Resource resource = getResource();

        List<Resource> resources = sut.list("example");

        assertThat(resources).hasSize(1);
        Resource result = resources.get(0);
        assertThat(result.getId().id()).isNotZero();
        assertThat(result.getFileId().id()).isNotZero();
        assertThat(result.getUrl()).isEqualTo(URL);
        assertThat(result.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));

    }

    @Test
    @Transactional
    public void testList_WhenSearchingForExistentUrlWithDifferentCase_ReturnList() {
        Resource resource = getResource();

        List<Resource> resources = sut.list("eXample");

        assertThat(resources).hasSize(1);
        Resource result = resources.get(0);
        assertThat(result.getId().id()).isNotZero();
        assertThat(result.getFileId().id()).isNotZero();
        assertThat(result.getUrl()).isEqualTo(URL);
        assertThat(result.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
    }

    @Test
    @Transactional
    public void testExists_WhenNotExists_ReturnFalse() throws MalformedURLException {
        getResource();

        assertThat(sut.exists(new URL("https://example2.com"))).isFalse();
    }

    @Test
    @Transactional
    public void testExists_WhenExists_ReturnTrue() {
        getResource();

        assertThat(sut.exists(url)).isTrue();
    }

    @Transactional
    public File getFile() {
        FilePropertiesPojo fileProperties = new FilePropertiesPojo();
        fileProperties
                .setName("test.txt")
                .setStorageName("8d4073ce-17d5-43e1-90a0-62e94fba1402")
                .setType("plain/text");

        return postgresFileRepository.save(fileProperties);
    }

    @Transactional
    public Resource getResource() {
        return sut.save(getFile(), url);
    }
}
