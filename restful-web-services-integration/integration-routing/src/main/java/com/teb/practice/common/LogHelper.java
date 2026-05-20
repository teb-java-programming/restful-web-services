package com.teb.practice.common;

import org.slf4j.Logger;

public class LogHelper {

    public static void log(Logger log,
                           String stage,
                           String correlationId,
                           String message) {

        log.info("stage={} | correlationId={} | message={}",
                stage,
                correlationId,
                message);
    }
}