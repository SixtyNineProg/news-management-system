package by.klimov.commentservice;

import by.klimov.commentservice.container.PostgresSqlContainerInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestCommentServiceApplication extends PostgresSqlContainerInitializer {

  public static void main(String[] args) {
    SpringApplication.from(CommentServiceApplication::main)
        .with(TestCommentServiceApplication.class)
        .run(args);
  }
}
