package by.klimov.newsservice.cache.factory;

import by.klimov.newsservice.cache.Cache;
import by.klimov.newsservice.cache.CacheType;

public interface CacheFactory<K, V> {

  Cache<K, V> getCache(CacheType cacheType);
}
