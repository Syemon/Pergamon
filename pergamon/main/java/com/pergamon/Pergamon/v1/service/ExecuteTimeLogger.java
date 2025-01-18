package com.pergamon.Pergamon.v1.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class ExecuteTimeLogger {

    private ExecuteTimeLogger() {
    }

     public static void logTime(Class<?> clazz, Runnable runnable) {
         String className = clazz.descriptorString();
         log.info("Starting {}", className);
         long start = System.currentTimeMillis();
         runnable.run();
         long end = System.currentTimeMillis();
         log.info("{} took {} ms", className, end - start);
     }

}
