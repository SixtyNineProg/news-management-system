package by.klimov.commentservice.cache.factory.impl;

import by.klimov.commentservice.cache.Cache;
import by.klimov.commentservice.cache.CacheType;
import by.klimov.commentservice.cache.factory.CacheFactory;
import by.klimov.commentservice.cache.impl.LFUCache;
import by.klimov.commentservice.cache.impl.LRUCache;
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
