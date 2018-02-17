package com.twitter.kszdev.reactive.backpressure.repository;

import com.twitter.kszdev.reactive.backpressure.Document;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
@Qualifier("jdbc")
class JdbcBasedDocumentRepository implements DocumentRepository {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcBasedDocumentRepository.class);

    private final DataSource dataSource;

    private final int fetchSize;

    JdbcBasedDocumentRepository(DataSource dataSource, @Value("${jdbc.fetchSize}") int fetchSize) {
        this.dataSource = dataSource;
        this.fetchSize = fetchSize;
    }

    public Flux<Document> findAll() {
        try {
            Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.setFetchSize(fetchSize);  // it's just a hint, your DB driver may ignore it
            ResultSet resultSet = statement.executeQuery("SELECT catalog_number, content FROM document");
            return Flux.fromIterable(() -> toResultSetIterator(resultSet, connection))
                    .doOnTerminate(() -> closeConnection(connection));
        } catch (SQLException e) {
            return Flux.error(e);
        }
    }

    private Iterator<Document> toResultSetIterator(ResultSet resultSet, Connection connection) {
        return new Iterator<Document>() {
            @Override
            public boolean hasNext() {
                boolean nextElementExists = hasNextElement(resultSet);
                if (!nextElementExists) {
                    closeConnection(connection);
                }
                return nextElementExists;
            }

            @Override
            public Document next() {
                try {
                    return new Document(resultSet.getInt(1), resultSet.getString(2));
                } catch (SQLException e) {
                    throw new IllegalStateException("Failed to get new element from query result set", e);
                }
            }

            private boolean hasNextElement(ResultSet resultSet) {
                try {
                    return resultSet.next();
                } catch (SQLException e) {
                    LOG.error("Failed to check if next element exists in query result set", e);
                    return false;
                }
            }
        };
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            LOG.error("Failed to close DB connection", e);
        }
    }

}
