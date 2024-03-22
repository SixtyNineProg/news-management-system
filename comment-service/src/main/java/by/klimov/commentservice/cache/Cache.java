package by.klimov.commentservice.cache;

import java.util.Optional;

public interface Cache<K, V> {

  void put(K key, V value);

  Optional<V> get(K key);

  void delete(K key);

  int size();

  boolean isEmpty();

  void clear();

  void setCapacity(int capacity);
}
