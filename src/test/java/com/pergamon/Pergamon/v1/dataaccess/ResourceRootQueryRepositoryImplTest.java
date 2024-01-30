package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentId;
import com.pergamon.Pergamon.v1.domain.ResourceId;
import com.pergamon.Pergamon.v1.domain.ResourceRoot;
import com.pergamon.Pergamon.v1.domain.ResourceRootQueryRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import com.pergamon.Pergamon.v1.service.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles(value = Profile.TEST_FLYWAY)
class ResourceRootQueryRepositoryImplTest extends PostgresTestContainerResourceTest {

    @Autowired
    private ResourceRootQueryRepository sut;

    @Test
    void listToRetry_whenEmpty() {
        assertThat(
                sut.listToRetry(
                        Set.of(ResourceStatus.ERROR, ResourceStatus.RETRY),
                        99999999,
                        3)
                )
                .isEmpty();
    }

    @Test
    void listToRetry_whenReceivedResults() throws MalformedURLException {
        // when
        List<ResourceRoot> result = sut.listToRetry(
                Set.of(ResourceStatus.ERROR, ResourceStatus.RETRY),
                30,
                3);

        // then
        assertThat(result).hasSize(2);

        Content expectedContent1 = Content.builder()
                .id(new ContentId(400))
                .name("test.txt")
                .storageName("405c6f9c-cd36-473d-88ea-5d1fbedde283")
                .type("plain/text")
                .createdAt(OffsetDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .build();

        ResourceRoot expectedResource1 = new ResourceRoot()
                .setId(new ResourceId(400))
                .setContent(expectedContent1)
                .setUrl(new URL("https://listResourceRootToRetry.com"))
                .setStatus(ResourceStatus.ERROR)
                .setCreatedAt(OffsetDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_DATE_TIME))
                .setAttemptNumber(0);

        Content expectedContent2 = Content.builder()
                .id(new ContentId(401))
                .name("test.txt")
                .storageName("f503a9d1-9e01-432d-9728-7eebd01ed739")
                .type("plain/text")
                .createdAt(OffsetDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_DATE_TIME))
                .build();

        ResourceRoot expectedResource2 = new ResourceRoot()
                .setId(new ResourceId(401))
                .setContent(expectedContent2)
                .setUrl(new URL("https://listResourceRootToRetry2.com"))
                .setStatus(ResourceStatus.RETRY)
                .setCreatedAt(OffsetDateTime.parse("2020-01-01T00:00:00Z", DateTimeFormatter.ISO_DATE_TIME))
                .setAttemptNumber(0);

        List<ResourceRoot> expectedResources = List.of(expectedResource1, expectedResource2);

        assertThat(result).usingRecursiveComparison()
                .ignoringFields(
                        "createdAt", "content.createdAt", "content.updatedAt", "content.name", "content.type", "content.storageName")
                .isEqualTo(expectedResources);
    }
}