package com.twitter.kszdev.reactive.backpressure;

import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;

import static java.lang.String.format;

@Entity
public class Document {

    @Id
    private int catalogNumber;

    private String content;

    private Document() {
        // required by hibernate
    }

    public Document(int catalogNumber) {
        this.catalogNumber = catalogNumber;
        this.content = UUID.randomUUID().toString();
    }

    public Document(int catalogNumber, String content) {
        this.catalogNumber = catalogNumber;
        this.content = content;
    }

    @Override
    public String toString() {
        return format("Document[catalogNumber=%s, content=%s]", catalogNumber, content);
    }
}
