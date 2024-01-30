package com.pergamon.Pergamon.v1.service;

import org.springframework.scheduling.annotation.Scheduled;

public class StoreResourceScheduler {

    @Scheduled(cron = "0 0/5 * * * ?")
    public void retryStoreResource() {

    }
}
