package com.pergamon.Pergamon.v1.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
public class StoreResourceScheduler {

    private final StoreResourceSchedulerProcessor storeResourceSchedulerProcessor;
    @Scheduled(cron = "0 0/2 * * * ?")
    public void retryStoreResource() {
        ExecuteTimeLogger.logTime(
                this.getClass(),
                storeResourceSchedulerProcessor::retry
        );
    }
}
