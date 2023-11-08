package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import com.pergamon.Pergamon.v1.service.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
@ActiveProfiles(value = Profile.TEST_FLYWAY)
class ResourceQueryRepositoryImplTest extends PostgresTestContainerResourceTest {

    private static final String URL_RAW = "https://example.com";
    private URL url;

    @Autowired
    ResourceQueryRepository sut;

    @BeforeEach
    public void setUp() throws MalformedURLException {
        url = new URL(URL_RAW);
    }

    @Test
    public void testList() {
        List<Resource> resources = sut.list();

        assertThat(resources.get(0).getId().id()).isEqualTo(100);
        assertThat(resources.get(0).getFileId()).isNull();
        assertThat(resources.get(0).getUrl()).isEqualTo("https://loremipsum.net/1");
    }

    @Test
    public void testList_WhenSearchingForNotExistentUrl_ReturnEmptyList() {
        Resource resource = sut.findByUrl(url).get();
        List<Resource> resources = sut.list("search");

        assertThat(resources).doesNotContain(resource);
    }

    @Test
    public void testList_WhenSearchingForExistentUrl_ReturnList() {
        Resource resource = sut.findByUrl(url).get();

        List<Resource> resources = sut.list("example");

        assertThat(resources).hasSize(1);
        Resource result = resources.get(0);
        assertThat(result.getId().id()).isNotZero();
        assertThat(result.getFileId().id()).isNotZero();
        assertThat(result.getUrl()).isEqualTo(URL_RAW);
        assertThat(result.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));

    }

    @Test
    public void testList_WhenSearchingForExistentUrlWithDifferentCase_ReturnList() {
        List<Resource> resources = sut.list("eXample");

        assertThat(resources).hasSize(1);
        Resource result = resources.get(0);
        assertThat(result.getId().id()).isNotZero();
        assertThat(result.getFileId().id()).isNotZero();
        assertThat(result.getUrl()).isEqualTo(URL_RAW);
        assertThat(result.getCreatedAt()).isCloseTo(LocalDateTime.now(), within(1, ChronoUnit.MINUTES));
    }
}