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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

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
                Set.of(ResourceStatus.RETRY),
                15,
                3);

        // then
        assertThat(result).hasSize(1);

        ResourceRoot resultResource = result.getFirst();
        assertThat(resultResource.getAttemptNumber()).isEqualTo(0);
        assertThat(resultResource.getCreatedAt().plusMinutes(15)).isCloseTo(OffsetDateTime.now(), within(5, ChronoUnit.MINUTES));
        assertThat(resultResource.getStatus()).isEqualTo(ResourceStatus.RETRY);
        assertThat(resultResource.getId()).isEqualTo(new ResourceId(403));
        assertThat(resultResource.getUrl()).isEqualTo(new URL("https://listResourceRootToRetry4.com"));
        
        Content resultContent = resultResource.getContent();
        assertThat(resultContent.getId()).isEqualTo(new ContentId(403));
        assertThat(resultContent.getName()).isEqualTo("test.txt");
        assertThat(resultContent.getStorageName()).isEqualTo("f503a9d1-9e01-432d-9728-7eebd01ed739");
        assertThat(resultContent.getType()).isEqualTo("plain/text");
        assertThat(resultContent.getCreatedAt().plusHours(1)).isCloseTo(OffsetDateTime.parse("2020-01-01T00:00:00Z"), within(1, ChronoUnit.HOURS));

        assertThat(resultContent.getUpdatedAt()).isNull();
    }

}