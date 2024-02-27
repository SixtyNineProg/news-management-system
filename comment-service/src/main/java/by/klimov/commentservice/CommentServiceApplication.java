package by.klimov.commentservice;

import by.klimov.commentservice.lucene.Indexer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CommentServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommentServiceApplication.class, args);
  }

  @Bean
  public ApplicationRunner buildIndex(Indexer indexer) {
    return (ApplicationArguments args) ->
        indexer.indexPersistedData("by.klimov.commentservice.entity.Comment");
  }
}
