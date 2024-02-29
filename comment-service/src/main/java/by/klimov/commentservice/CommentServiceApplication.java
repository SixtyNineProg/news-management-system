package by.klimov.commentservice;

import by.klimov.commentservice.repository.impl.SearchRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class CommentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommentServiceApplication.class, args);
  }
}
