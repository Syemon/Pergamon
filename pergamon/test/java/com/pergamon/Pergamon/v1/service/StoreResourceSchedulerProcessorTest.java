package com.pergamon.Pergamon.v1.service;

import com.pergamon.Pergamon.v1.domain.Content;
import com.pergamon.Pergamon.v1.domain.Resource;
import com.pergamon.Pergamon.v1.domain.ResourceRoot;
import com.pergamon.Pergamon.v1.domain.ResourceRootQueryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.event.ApplicationEventMulticaster;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreResourceSchedulerProcessorTest {

    @InjectMocks
    private StoreResourceSchedulerProcessor sut;
    @Mock
    private ResourceRootQueryRepository resourceRootQueryRepository;
    @Mock
    private ApplicationEventMulticaster applicationEventMulticaster;

    @Mock
    private EventMapper eventMapper;

    @Test
    void retry_shouldDoNothing_whenNoResourcesToRetry() {
        // given
        when(resourceRootQueryRepository.listToRetry(any(), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        // when
        sut.retry();

        // then
        Mockito.verifyNoInteractions(applicationEventMulticaster);
    }

    @Test
    void retry_shouldSendStoreResourceEvent_whenThereIsAtLeastOneResourceToStore() {
        // given
        when(resourceRootQueryRepository.listToRetry(any(), anyInt(), anyInt()))
                .thenReturn(List.of(new ResourceRoot(), new ResourceRoot()));
        when(eventMapper.mapResourceRootToStoreResourceEvent(any()))
                .thenReturn(
                        new StoreResourceEvent(
                                this,
                                new StoreResourcePayload(
                                        new Resource(),
                                        Content.builder().build())
                        )
                );

        // when
        sut.retry();

        // then
        verify(applicationEventMulticaster, Mockito.times(2))
                .multicastEvent(any(StoreResourceEvent.class));
    }
}