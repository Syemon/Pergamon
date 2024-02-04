package com.pergamon.Pergamon.v1.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ExecuteTimeLogger {

    private ExecuteTimeLogger() {
    }

     public static void logTime(String message, Runnable runnable) {
         log.info("Starting {}", message);
         long start = System.currentTimeMillis();
         runnable.run();
         long end = System.currentTimeMillis();
         log.info("{} took {} ms", message, end - start);
     }

}
