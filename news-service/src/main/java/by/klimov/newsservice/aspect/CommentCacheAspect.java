package by.klimov.newsservice.aspect;

import by.klimov.newsservice.cache.Cache;
import by.klimov.newsservice.entity.News;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommentCacheAspect {

  private final Cache<Integer, News> cache;

  @Around("execution(* org.springframework.data.repository.CrudRepository.save(..))")
  public Object doCreateProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
    News news = (News) joinPoint.getArgs()[0];
    log.debug("Aspect around create news. {}", news);
    int rowCount = (Integer) joinPoint.proceed();
    if (rowCount == 1) {
      cache.put(news.getId(), news);
    }
    return rowCount;
  }

  @Around("execution(* org.springframework.data.repository.CrudRepository.delete(..))")
  public Object doDeleteProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
    Integer id = (Integer) joinPoint.getArgs()[0];
    log.debug("Aspect around delete news with id = {}", id);
    int rowCount = (Integer) joinPoint.proceed();
    if (rowCount == 1) {
      cache.delete(id);
    }
    return rowCount;
  }

  @SuppressWarnings("unchecked")
  @Around("execution(* org.springframework.data.repository.CrudRepository.findById(..))")
  public Object doReadProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
    Integer id = (Integer) joinPoint.getArgs()[0];
    log.debug("Aspect around read comment by id = {}", id);
    Optional<News> optionalNews = cache.get(id);
    if (optionalNews.isEmpty()) {
      optionalNews = (Optional<News>) joinPoint.proceed();
      if (optionalNews.isPresent()) {
        News news = optionalNews.get();
        cache.put(news.getId(), news);
      }
    }
    return optionalNews;
  }
}
