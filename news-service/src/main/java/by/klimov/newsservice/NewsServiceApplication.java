package by.klimov.newsservice;

import by.klimov.newsservice.repository.impl.SearchRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
public class NewsServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NewsServiceApplication.class, args);
  }
}
