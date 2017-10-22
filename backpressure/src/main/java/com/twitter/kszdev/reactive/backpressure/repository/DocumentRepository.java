package com.twitter.kszdev.reactive.backpressure.repository;

import com.twitter.kszdev.reactive.backpressure.Document;
import reactor.core.publisher.Flux;

public interface DocumentRepository {

    Flux<Document> findAll();

}
