package by.klimov.commentservice.aspect;

import by.klimov.commentservice.cache.Cache;
import by.klimov.commentservice.entity.Comment;
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

  private final Cache<Integer, Comment> cache;

  @Around("execution(* org.springframework.data.repository.CrudRepository.save(..))")
  public Object doCreateProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
    Comment bank = (Comment) joinPoint.getArgs()[0];
    log.debug("Aspect around create comment. {}", bank);
    int rowCount = (Integer) joinPoint.proceed();
    if (rowCount == 1) {
      cache.put(bank.getId(), bank);
    }
    return rowCount;
  }

  @Around("execution(* org.springframework.data.repository.CrudRepository.delete(..))")
  public Object doDeleteProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
    Integer id = (Integer) joinPoint.getArgs()[0];
    log.debug("Aspect around delete comment with id = {}", id);
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
    Optional<Comment> optionalComment = cache.get(id);
    if (optionalComment.isEmpty()) {
      optionalComment = (Optional<Comment>) joinPoint.proceed();
      if (optionalComment.isPresent()) {
        Comment comment = optionalComment.get();
        cache.put(comment.getId(), comment);
      }
    }
    return optionalComment;
  }
}
