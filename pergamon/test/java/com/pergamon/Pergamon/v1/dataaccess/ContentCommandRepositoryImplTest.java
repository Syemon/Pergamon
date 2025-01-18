package com.pergamon.Pergamon.v1.dataaccess;

import com.pergamon.Pergamon.PostgresTestContainerResourceTest;
import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentCommand;
import com.pergamon.Pergamon.v1.service.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SpringBootTest
@ActiveProfiles(value = Profile.TEST_FLYWAY)
class ContentCommandRepositoryImplTest extends PostgresTestContainerResourceTest {

    private static final String STORAGE_NAME = UUID.randomUUID().toString();
    public static final String NAME = "NAME";
    public static final String CONTENT_TYPE = "text/html";

    @Autowired
    private ContentCommandRepositoryImpl sut;

    @Test
    void createContent() {
        // given
        ContentCommand contentCommand = ContentCommand.builder()
                .name(NAME)
                .storageName(STORAGE_NAME)
                .type(CONTENT_TYPE)
                .build();

        // when
        Content result = sut.createContent(contentCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getCreatedAt()).isCloseTo(OffsetDateTime.now(), within(1, ChronoUnit.MINUTES));
        assertThat(result.getType()).isEqualTo(CONTENT_TYPE);
        assertThat(result.getStorageName()).isEqualTo(STORAGE_NAME);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId().id()).isNotZero();
        assertThat(result.getUpdatedAt()).isNull();
    }
}