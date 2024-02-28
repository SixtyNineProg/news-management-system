package by.klimov.commentservice.service;

import java.util.Optional;

public interface CrudService<T, I> {

  T create(T entity);

  Optional<T> readById(I id);

  T update(T entity);

  void deleteById(I id);
}
