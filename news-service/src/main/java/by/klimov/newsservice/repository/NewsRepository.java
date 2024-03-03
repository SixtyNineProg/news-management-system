package by.klimov.newsservice.repository;

import by.klimov.newsservice.entity.News;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends SearchRepository<News, Integer> {}
