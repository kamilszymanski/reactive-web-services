package com.twitter.kszdev.reactive.backpressure.repository;

import com.twitter.kszdev.reactive.backpressure.Document;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@Qualifier("jpa")
class JpaBasedDocumentRepository implements DocumentRepository {

    private final EntityManager entityManager;

    JpaBasedDocumentRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // loads the whole dataset into memory before Flux is created
    public Flux<Document> findAll() {
        return Flux.fromIterable(
                entityManager.createQuery("from Document d")
                        .getResultList());
    }

}
