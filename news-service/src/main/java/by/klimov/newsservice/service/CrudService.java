package by.klimov.newsservice.service;

public interface CrudService<T, I> {

  T create(T entity);

  T readById(I id, Integer pageSize);

  T update(T entity);

  void deleteById(I id);
}
