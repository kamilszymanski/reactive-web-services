package com.twitter.kszdev.reactive.backpressure.repository;

import com.twitter.kszdev.reactive.backpressure.Document;
import org.hibernate.jpa.HibernateEntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import javax.persistence.EntityManager;
import java.util.stream.Stream;

@Repository
@Qualifier("hibernate")
class HibernateBasedDocumentRepository implements DocumentRepository {

    private final HibernateEntityManager entityManager;

    HibernateBasedDocumentRepository(EntityManager entityManager) {
        this.entityManager = entityManager.unwrap(HibernateEntityManager.class);
    }

    // entities won't be garbage collected as long as they are managed
    public Flux<Document> findAll() {
        Stream<Document> resultStream = entityManager.createQuery("from Document d")
                .stream();
        return Flux.fromStream(resultStream)
                .doOnTerminate(resultStream::close);
    }

}
