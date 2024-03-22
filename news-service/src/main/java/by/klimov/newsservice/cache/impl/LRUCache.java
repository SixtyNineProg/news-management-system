package by.klimov.newsservice.cache.impl;

import by.klimov.newsservice.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LRUCache<K, V> implements Cache<K, V> {

  private final Map<K, Node<K, V>> cache = new HashMap<>();
  private final Node<K, V> head = new Node<>(null, null);
  private final Node<K, V> tail = new Node<>(null, null);
  private int capacity = 10;

  public LRUCache() {
    this.head.next = tail;
    this.tail.prev = head;
  }

  @Override
  public void put(K key, V value) {
    Node<K, V> node = cache.get(key);
    if (node != null) {
      node.value = value;
      remove(node);
      add(node);
    } else {
      if (cache.size() == capacity) {
        cache.remove(tail.prev.key);
        remove(tail.prev);
      }
      Node<K, V> newNode = new Node<>(key, value);
      cache.put(key, newNode);
      add(newNode);
    }
  }

  @Override
  public Optional<V> get(K key) {
    Node<K, V> node = cache.get(key);
    if (node == null) {
      return Optional.empty();
    }
    remove(node);
    add(node);
    return Optional.ofNullable(node.value);
  }

  @Override
  public void delete(K key) {
    Node<K, V> item = cache.get(key);
    if (item != null) {
      remove(item);
      cache.remove(key);
    }
  }

  @Override
  public int size() {
    return cache.size();
  }

  @Override
  public boolean isEmpty() {
    return cache.isEmpty();
  }

  @Override
  public void clear() {
    cache.clear();
  }

  @Override
  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  private void add(Node<K, V> node) {
    Node<K, V> headNext = head.next;
    head.next = node;
    node.prev = head;
    node.next = headNext;
    headNext.prev = node;
  }

  private void remove(Node<K, V> node) {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }

  private static class Node<K, V> {
    private final K key;
    private V value;
    private Node<K, V> prev;
    private Node<K, V> next;

    private Node(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }
}
