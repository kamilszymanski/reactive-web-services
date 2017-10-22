package com.twitter.kszdev.reactive.backpressure;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.twitter.kszdev.reactive.backpressure.Sleeper.sleepFor;
import static java.util.concurrent.TimeUnit.MINUTES;

@Component
class ApplicationTerminationProtector implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        sleepFor(5, MINUTES);
    }
}
