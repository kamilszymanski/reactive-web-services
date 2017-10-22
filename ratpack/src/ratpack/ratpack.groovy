import groovy.util.logging.Slf4j
import ratpack.exec.Blocking
import ratpack.exec.Promise
import ratpack.handling.Context
import ratpack.handling.Handler
import rx.Observable

import static java.util.concurrent.TimeUnit.SECONDS
import static ratpack.groovy.Groovy.ratpack
import static ratpack.rx.RxRatpack.observe

ratpack {
    bindings {
        DocumentRepository documentRepository = new DocumentRepository()
        bindInstance(AsyncDocumentsLoader, new AsyncDocumentsLoader(documentRepository: documentRepository))
        bindInstance(SequentialDocumentsLoader, new SequentialDocumentsLoader(documentRepository: documentRepository))
    }
    handlers {
        get("sequential", SequentialDocumentsLoader)
        get("async", AsyncDocumentsLoader)
    }
}

class SequentialDocumentsLoader implements Handler {

    private DocumentRepository documentRepository

    @Override
    void handle(Context context) {
        def documentIds = [1, 2, 3, 4]
        Observable.from(documentIds)
                .flatMap { id -> observe(documentRepository.findById(id)) }
                .toList()
                .subscribe { documents -> context.render(documents.join(',')) }
    }
}

class AsyncDocumentsLoader implements Handler {

    private DocumentRepository documentRepository

    @Override
    void handle(Context context) {
        def documentIds = [1, 2, 3, 4]
        Observable.from(documentIds)
                .forkEach()
                .flatMap { id -> observe(documentRepository.findById(id)) }
                .bindExec()
                .toList()
                .subscribe { documents -> context.render(documents.join(',')) }
    }
}

@Slf4j
class DocumentRepository {

    private final Random random = new Random()

    Promise<Integer> findById(int documentId) {
        log.info("Loading Document[id=${documentId}]")
        return Blocking.get {
            int loadTime = random.nextInt(4)
            SECONDS.sleep(loadTime)
            log.info("Loaded Document[id=${documentId}] in ${loadTime} seconds")
            return documentId
        }
    }
}
