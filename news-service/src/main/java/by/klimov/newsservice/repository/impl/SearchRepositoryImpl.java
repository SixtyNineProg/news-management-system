package by.klimov.newsservice.repository.impl;

import by.klimov.newsservice.repository.SearchRepository;
import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SearchRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
    implements SearchRepository<T, ID> {

  private final EntityManager entityManager;

  public SearchRepositoryImpl(
      JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  @Override
  public Page<T> searchBy(String text, Pageable pageable, String... fields) {
    SearchResult<T> result =
        getSearchResult(
            text, Math.toIntExact(pageable.getOffset()), pageable.getPageSize(), fields);

    List<T> hits = result.hits();

    return new PageImpl<>(hits, pageable, result.total().hitCount());
  }

  private SearchResult<T> getSearchResult(
      String text, Integer offset, Integer limit, String[] fields) {
    SearchSession searchSession = Search.session(entityManager);

    return searchSession
        .search(getDomainClass())
        .where(f -> f.match().fields(fields).matching(text).fuzzy(2))
        .fetch(offset, limit);
  }
}
