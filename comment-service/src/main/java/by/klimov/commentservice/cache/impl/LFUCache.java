package by.klimov.commentservice.cache.impl;

import by.klimov.commentservice.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class LFUCache<K, V> implements Cache<K, V> {

  private final Map<K, Node<K, V>> cache = new HashMap<>();
  private Node<K, V> head;
  private Node<K, V> tail;
  private int capacity = 10;

  @Override
  public Optional<V> get(K key) {
    if (Objects.isNull(cache.get(key))) {
      return Optional.empty();
    }
    Node<K, V> item = cache.get(key);
    removeNode(item);
    item.frequency = item.frequency + 1;
    addNodeWithUpdatedFrequency(item);
    return Optional.ofNullable(item.value);
  }

  @Override
  public void delete(K key) {
    Node<K, V> item = cache.get(key);
    if (item != null) {
      removeNode(item);
      cache.remove(item.key);
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

  @Override
  public void put(K key, V value) {
    if (cache.containsKey(key)) {
      Node<K, V> item = cache.get(key);
      item.value = value;
      item.frequency = item.frequency + 1;
      removeNode(item);
      addNodeWithUpdatedFrequency(item);
    } else {
      if (cache.size() >= capacity) {
        cache.remove(head.key);
        removeNode(head);
      }
      Node<K, V> node = new Node<>(key, value, 1);
      addNodeWithUpdatedFrequency(node);
      cache.put(key, node);
    }
  }

  private void removeNode(Node<K, V> node) {
    if (node.prev != null) {
      node.prev.next = node.next;
    } else {
      head = node.next;
    }
    if (node.next != null) {
      node.next.prev = node.prev;
    } else {
      tail = node.prev;
    }
  }

  private void addNodeWithUpdatedFrequency(Node<K, V> node) {
    if (tail != null && head != null) {
      Node<K, V> temp = head;
      while (temp != null) {
        if (temp.frequency > node.frequency) {
          if (temp == head) {
            node.next = temp;
            temp.prev = node;
            head = node;
            break;
          } else {
            node.next = temp;
            node.prev = temp.prev;
            temp.prev = node;
            temp.prev.next = node;
            break;
          }
        } else {
          temp = temp.next;
          if (temp == null) {
            tail.next = node;
            node.prev = tail;
            node.next = null;
            tail = node;
            break;
          }
        }
      }
    } else {
      tail = node;
      head = tail;
    }
  }

  private static class Node<K, V> {
    private final K key;
    private V value;
    private int frequency;
    private Node<K, V> prev;
    private Node<K, V> next;

    private Node(K key, V value, int frequency) {
      this.key = key;
      this.value = value;
      this.frequency = frequency;
    }
  }
}
