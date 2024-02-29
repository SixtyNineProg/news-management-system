package by.klimov.commentservice;

import by.klimov.commentservice.container.PostgresSqlContainerInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestCommentServiceApplication extends PostgresSqlContainerInitializer {

  public static void main(String[] args) {
    SpringApplication.from(CommentServiceApplication::main)
        .with(TestCommentServiceApplication.class)
        .run(args);
  }
}
