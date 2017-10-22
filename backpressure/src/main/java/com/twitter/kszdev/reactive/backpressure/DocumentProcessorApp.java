package com.twitter.kszdev.reactive.backpressure;

import com.twitter.kszdev.reactive.backpressure.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.twitter.kszdev.reactive.backpressure.Sleeper.sleepFor;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootApplication
public class DocumentProcessorApp implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentProcessorApp.class);

    public static void main(String[] args) {
        SpringApplication.run(DocumentProcessorApp.class);
    }

    @Autowired
    @Qualifier("generator")
    private DocumentRepository documentRepository;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        documentRepository.findAll()
                .subscribe(document -> {
                    LOG.warn("Processing: {}", document);
                    sleepFor(1, SECONDS);
                    LOG.info("Processed: {}", document);
                });
    }
}
