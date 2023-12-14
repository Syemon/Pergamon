package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceCommandRepository;
import com.pergamon.Pergamon.v1.domain.ResourceStatus;
import com.pergamon.Pergamon.v1.domain.StorageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreResourceListenerTest {

    @InjectMocks
    private StoreResourceListener sut;

    @Mock
    private StorageRepository storageLocalRepository;
    @Mock
    private ResourceCommandRepository resourceCommandRepository;

    @Test
    void store() {
        //given
        Resource resource = new Resource().setStatus(ResourceStatus.NEW);
        Content content = Content.builder().build();
        StoreResourceEvent event = new StoreResourceEvent(this,
                StoreResourcePayload.builder()
                        .resource(resource)
                        .content(content)
                        .build());
        Resource expectedResource = new Resource().setStatus(ResourceStatus.DONE);
        when(storageLocalRepository.store(resource, content)).thenReturn(new Resource().setStatus(ResourceStatus.DONE));

        //when
        sut.store(event);

        //then
        verify(resourceCommandRepository, times(1)).saveResource(expectedResource);
    }
}