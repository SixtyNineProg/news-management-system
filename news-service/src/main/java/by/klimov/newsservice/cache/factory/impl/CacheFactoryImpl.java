package by.klimov.newsservice.cache.factory.impl;

import by.klimov.newsservice.cache.Cache;
import by.klimov.newsservice.cache.CacheType;
import by.klimov.newsservice.cache.factory.CacheFactory;
import by.klimov.newsservice.cache.impl.LFUCache;
import by.klimov.newsservice.cache.impl.LRUCache;
import org.hibernate.cache.CacheException;

public class CacheFactoryImpl<K, V> implements CacheFactory<K, V> {

  @Override
  public Cache<K, V> getCache(CacheType cacheType) {
    if (cacheType.equals(CacheType.LFU)) {
      return new LFUCache<>();
    } else if (cacheType.equals(CacheType.LRU)) {
      return new LRUCache<>();
    } else {
      throw new CacheException("Cache not found");
    }
  }
}
