package by.klimov.commentservice.lucene;

import by.klimov.commentservice.exception.IndexException;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@RequiredArgsConstructor
public class Indexer {

  private static final int THREAD_NUMBER = 4;
  private final EntityManager entityManager;

  public void indexPersistedData(String indexClassName) throws IndexException {
    try {
      SearchSession searchSession = Search.session(entityManager);

      Class<?> classToIndex = Class.forName(indexClassName);
      MassIndexer indexer =
          searchSession.massIndexer(classToIndex).threadsToLoadObjects(THREAD_NUMBER);

      indexer.startAndWait();
    } catch (ClassNotFoundException e) {
      throw new IndexException("Invalid class " + indexClassName, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IndexException("Index Interrupted", e);
    }
  }
}
