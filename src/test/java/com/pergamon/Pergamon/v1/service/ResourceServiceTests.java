package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.dataaccess.ContentEntity;
import com.pergamon.Pergamon.v1.dataaccess.PostgresFileRepository;
import com.pergamon.Pergamon.v1.dataaccess.PostgresResourceRepository;
import com.pergamon.Pergamon.v1.dataaccess.ResourceEntity;
import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.ContentCommandRepository;
import com.pergamon.Pergamon.v1.domain.ContentId;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.event.ApplicationEventMulticaster;

import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTests {
    public static final String CONTENT_TYPE = "plain/text";
    public static final String FILE_NAME = "file.txt";
    private ContentEntity contentEntity;

    @InjectMocks
    private ResourceService sut;

    @Mock
    private URL url;

    @Mock
    private org.springframework.core.io.Resource fileResource;

    @Mock
    private ContentService contentService;

    @Mock
    private PostgresFileRepository fileDao;

    @Mock
    private PostgresResourceRepository resourceDao;

    @Mock
    private ResourceCollectionModelCreator resourceResourceCreator;

    @Mock
    private ResourceCommandRepository resourceCommandRepository;

    @Mock
    private ContentCommandRepository contentCommandRepository;
    @Mock
    private ApplicationEventMulticaster applicationEventMulticaster;
    @Spy
    private EventMapper eventMapper;


    @BeforeEach
    public void setUp() {
        contentEntity = ContentEntity.builder().build();
        contentEntity.setType(CONTENT_TYPE);
        contentEntity.setName(FILE_NAME);
    }

    @Test
    public void testCreate_WhenCorrectData() {
        // given
        when(contentCommandRepository.createContent(any())).thenReturn(
                Content.builder().id(new ContentId(1)).build());

        Resource resource = new Resource().setUrl(url);

        // when
        sut.create(resource);

        // then
        verify(resourceCommandRepository, times(1)).saveResource(resource);
        verify(applicationEventMulticaster).multicastEvent(any(StoreResourceEvent.class));
    }

    @Test
    public void create_shouldMarkResourceAsFailed_whenContentIsNotValid() {
        // given
        ValidationError validationError = new ValidationError(List.of("failureMessage"));
        when(contentService.validateInitialContent(any(URL.class))).thenReturn(Optional.of(validationError));

        Resource resource = new Resource().setUrl(url);

        // when
        assertThatException()
                .isThrownBy(() -> sut.create(resource))
                .isInstanceOf(IllegalArgumentException.class)
                .withMessage("Received content is not valid. Details: [failureMessage]");

        // then
        verify(resourceCommandRepository, times(1)).saveResource(resource);
        verify(contentCommandRepository, never()).createContent(any());
        verifyNoInteractions(applicationEventMulticaster);

        assertThat(resource.getStatus()).isEqualTo(ResourceStatus.FAILED);
    }

    @Test
    public void testList_WhenNoResources() {
        List<ResourceEntity> resources = getFilledResourceList();
        when(resourceDao.list()).thenReturn(resources);

        assertSame(resources, sut.list());
    }

    @Test
    public void testList_WhenNoResourcesWithSearch() {
        List<ResourceEntity> resources = new ArrayList<>();
        when(resourceDao.list("www")).thenReturn(resources);

        assertSame(resources, sut.list("www"));
    }

    @Test
    public void testList_WhenWithResourceWithSearch() {
        List<ResourceEntity> resources = getFilledResourceList();
        when(resourceDao.list("www")).thenReturn(resources);

        assertSame(resources, sut.list("www"));
    }

    @Test
    public void testDownload() {
        when(fileDao.findByUrl(any(URL.class))).thenReturn(contentEntity);

        sut.download(url);
    }

    private List<ResourceEntity> getFilledResourceList() {
        List<ResourceEntity> resources = new ArrayList<>();
        resources.add(new ResourceEntity()
                .setId(1)
                .setFileId(1)
                .setUrl("name")
                .setCreatedAt(OffsetDateTime.now()));

        return resources;
    }
}
