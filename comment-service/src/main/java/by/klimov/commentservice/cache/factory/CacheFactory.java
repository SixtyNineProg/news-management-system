package by.klimov.commentservice.cache.factory;

import by.klimov.commentservice.cache.Cache;
import by.klimov.commentservice.cache.CacheType;

public interface CacheFactory<K, V> {

  Cache<K, V> getCache(CacheType cacheType);
}
