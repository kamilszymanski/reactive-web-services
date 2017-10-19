package com.twitter.kszdev.reactive.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.twitter.kszdev.reactive.webflux.Sleeper.sleepFor;
import static java.util.concurrent.TimeUnit.SECONDS;

@RestController
@SpringBootApplication
public class ReactiveWebApp {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveWebApp.class);
    }

    // blocks event loop on Netty
    @GetMapping("tomcatFavour")
    Mono<String> fetchValueBlockingOnSameThread() {
        return Mono.fromCallable(this::fetchValue);
    }

    // creates additional threads on Tomcat
    @GetMapping("nettyFavour")
    Mono<String> fetchValueBlockingOnSeparateThread() {
        return Mono.fromCallable(this::fetchValue)
                .subscribeOn(Schedulers.elastic());
    }

    // blocking operation
    private String fetchValue() {
        sleepFor(1, SECONDS);
        return "42";
    }
}
