package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommand;
import com.pergamon.Pergamon.v1.domain.ResourceQueryRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import com.pergamon.Pergamon.v1.service.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
@ActiveProfiles(value = Profile.TEST_FLYWAY)
public class ResourceCommandRepositoryImplTest extends PostgresTestContainerResourceTest {

    public static final String URL = "https://resourcecommandrepository.com";

    @Autowired
    private ResourceCommandRepositoryImpl sut;

    @Autowired
    private ResourceQueryRepository resourceQueryRepository;
    private URL url;

    @Test
    void createResource() throws MalformedURLException {
        // given
        url = new URL(URL);
        ResourceCommand resourceCommand = new ResourceCommand().setUrl(url);

        // when
        Resource result = sut.createResource(resourceCommand);

        // then
        Optional<Resource> expectedResource = resourceQueryRepository.findByUrl(url);

        assertThat(expectedResource).isPresent();

        assertThat(result.getId().id()).isNotZero();
        assertThat(result.getId()).isEqualTo(expectedResource.get().getId());
        assertThat(result.getCreatedAt()).isCloseTo(OffsetDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(result.getModifiedAt()).isNull();
        assertThat(result.getUrl()).isEqualTo(URL);
        assertThat(result.getAttemptNumber()).isZero();
        assertThat(result.getStatus()).isEqualTo(ResourceStatus.NEW);
        assertThat(result.getFileId()).isNull();
    }

    @Test
    void saveResource() throws MalformedURLException {
        // given
        Optional<Resource> resource = resourceQueryRepository.findByUrl(new URL("https://resourcecommandrepositorysave.com"));
        assertThat(resource).isPresent();

        // when
        Resource result = sut.saveResource(resource.get());

        assertThat(result.getId().id()).isEqualTo(300);
        assertThat(result.getCreatedAt()).isCloseTo(OffsetDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(result.getModifiedAt()).isCloseTo(OffsetDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(result.getUrl()).isEqualTo("https://resourcecommandrepositorysave.com");
        assertThat(result.getAttemptNumber()).isZero();
        assertThat(result.getStatus()).isEqualTo(ResourceStatus.RETRY);
        assertThat(result.getFileId()).isNull();
    }
}