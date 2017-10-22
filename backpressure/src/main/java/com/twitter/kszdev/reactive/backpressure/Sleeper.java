package com.twitter.kszdev.reactive.backpressure;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sleeper {

    private static final Logger LOG = LoggerFactory.getLogger(Sleeper.class);

    public static void sleepFor(int timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            LOG.error("Interrupted sleep", e);
        }
    }
}
