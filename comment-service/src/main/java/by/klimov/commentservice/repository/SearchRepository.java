package by.klimov.commentservice.repository;

import java.io.Serializable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SearchRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

  Page<T> searchBy(String text, PageRequest pageRequest, String... fields);
}
