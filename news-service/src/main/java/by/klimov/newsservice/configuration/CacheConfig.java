package by.klimov.newsservice.configuration;

import by.klimov.newsservice.cache.Cache;
import by.klimov.newsservice.cache.CacheType;
import by.klimov.newsservice.cache.factory.impl.CacheFactoryImpl;
import by.klimov.newsservice.entity.News;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  @Value("${cache.type}")
  private CacheType cacheType;

  @Value("${cache.capacity}")
  private Integer cacheCapacity;

  @Bean
  public Cache<Integer, News> cacheBean() {
    Cache<Integer, News> cache = new CacheFactoryImpl<Integer, News>().getCache(cacheType);
    cache.setCapacity(cacheCapacity);
    return cache;
  }
}
