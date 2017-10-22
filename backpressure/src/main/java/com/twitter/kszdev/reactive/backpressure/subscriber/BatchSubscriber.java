package com.twitter.kszdev.reactive.backpressure.subscriber;

import com.twitter.kszdev.reactive.backpressure.Document;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.twitter.kszdev.reactive.backpressure.Sleeper.sleepFor;
import static java.util.concurrent.TimeUnit.SECONDS;

// don't implement such subscribers for other purposes than education, in real projects use existing operators instead
public class BatchSubscriber extends BaseSubscriber<Document> {

    private static final Logger LOG = LoggerFactory.getLogger(BatchSubscriber.class);

    private final AtomicInteger requested = new AtomicInteger(0);
    private final AtomicInteger processed = new AtomicInteger(0);
    private final int batchSize;

    public BatchSubscriber(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    protected void hookOnSubscribe(Subscription subscription) {
        request(requested.addAndGet(batchSize));
    }

    @Override
    protected void hookOnNext(Document document) {
        onDocument(document)
                .subscribeOn(Schedulers.elastic())
                .subscribe(doc -> {
                    sleepFor(1, SECONDS);
                    LOG.info("Processed: {}", doc);
                    requestNewBatchWhenDoneProcessingCurrentOne();
                });
    }

    private Mono<Document> onDocument(Document document) {
        return Mono.fromCallable(() -> {
            LOG.warn("Processing: {}", document);
            return document;
        });
    }

    private void requestNewBatchWhenDoneProcessingCurrentOne() {
        int processedDocuments = processed.incrementAndGet();
        if (processedDocuments == requested.get()) {
            request(batchSize);
            requested.addAndGet(batchSize);
        }
    }
}
