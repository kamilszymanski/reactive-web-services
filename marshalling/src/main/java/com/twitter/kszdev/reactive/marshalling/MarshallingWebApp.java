package com.twitter.kszdev.reactive.marshalling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static java.time.Duration.ofSeconds;

@SpringBootApplication
@RestController
public class MarshallingWebApp {

    public static void main(String[] args) {
        SpringApplication.run(MarshallingWebApp.class);
    }

    @GetMapping(path = "array", produces = "application/json")
    Flux<Document> jsonArray() {
        return fetchDocuments();
    }

    @GetMapping(path = "objectStream", produces = "application/stream+json")
    Flux<Document> streamOfJsonObjects() {
        return fetchDocuments();
    }

    @GetMapping(path = "sseStream", produces = "text/event-stream")
    Flux<Document> streamOfServerSentEvents() {
        return fetchDocuments();
    }

    private Flux<Document> fetchDocuments() {
        return Flux.interval(ofSeconds(1))
                .take(9)
                .map(i -> i.intValue() + 1)
                .map(Document::new);
    }
}
