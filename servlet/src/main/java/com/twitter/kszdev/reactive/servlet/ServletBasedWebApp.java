package com.twitter.kszdev.reactive.servlet;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import static com.twitter.kszdev.reactive.servlet.Sleeper.sleepFor;
import static java.util.concurrent.TimeUnit.SECONDS;

@RestController
@SpringBootApplication
public class ServletBasedWebApp {

    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        SpringApplication.run(ServletBasedWebApp.class);
    }

    // can serve only server.tomcat.max-threads requests in parallel
    @GetMapping("workerThread")
    String fetchValueBlockingOnWorkerThread() {
        return fetchValue();
    }

    // little control over threads
    @GetMapping("springManagedThread")
    Callable<String> fetchValueBlockingOnSeparateThreadManagedBySpring() {
        return this::fetchValue;
    }

    // low signal-to-noise ratio
    @GetMapping("controlledThreadPool")
    DeferredResult<String> fetchValueBlockingOnControlledThreadPool() {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        fetchValueAsync(deferredResult);
        return deferredResult;
    }

    // blocking operation
    private String fetchValue() {
        sleepFor(1, SECONDS);
        return "42";
    }

    private void fetchValueAsync(DeferredResult<String> deferredResult) {
        CompletableFuture.supplyAsync(this::fetchValue, threadPool)
                .whenCompleteAsync((result, throwable) -> {
                    if (throwable != null) {
                        deferredResult.setErrorResult(throwable);
                    } else {
                        deferredResult.setResult(result);
                    }
                });
    }
}
