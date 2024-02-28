package by.klimov.commentservice.configuration;

import by.klimov.commentservice.cache.Cache;
import by.klimov.commentservice.cache.CacheType;
import by.klimov.commentservice.cache.factory.impl.CacheFactoryImpl;
import by.klimov.commentservice.entity.Comment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class CacheConfig {

  @Value("${cache.type}")
  private CacheType cacheType;

  @Value("${cache.capacity}")
  private Integer cacheCapacity;

  @Bean
  public Cache<Integer, Comment> cacheBean() {
    Cache<Integer, Comment> cache = new CacheFactoryImpl<Integer, Comment>().getCache(cacheType);
    cache.setCapacity(cacheCapacity);
    return cache;
  }
}
