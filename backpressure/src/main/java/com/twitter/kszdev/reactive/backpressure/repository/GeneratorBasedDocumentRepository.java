package com.twitter.kszdev.reactive.backpressure.repository;

import com.twitter.kszdev.reactive.backpressure.Document;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@Qualifier("generator")
class GeneratorBasedDocumentRepository implements DocumentRepository {

    public Flux<Document> findAll() {
        return Flux.range(1, 9)
                .map(Document::new);
    }

}
