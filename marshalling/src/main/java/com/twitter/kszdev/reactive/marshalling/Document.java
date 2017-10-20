package com.twitter.kszdev.reactive.marshalling;

import java.util.UUID;

import static java.lang.String.format;

class Document {

    private final int catalogNumber;

    private final String content;

    Document(int catalogNumber) {
        this.catalogNumber = catalogNumber;
        this.content = UUID.randomUUID().toString();
    }

    @Override
    public String toString() {
        return format("Document[catalogNumber=%s, content=%s]", catalogNumber, content);
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public String getContent() {
        return content;
    }
}
