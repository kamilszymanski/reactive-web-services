package com.twitter.kszdev.reactive.servlet;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Sleeper {

    private static final Logger LOG = LoggerFactory.getLogger(Sleeper.class);

    static void sleepFor(int timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            LOG.error("Interrupted sleep", e);
        }
    }
}
