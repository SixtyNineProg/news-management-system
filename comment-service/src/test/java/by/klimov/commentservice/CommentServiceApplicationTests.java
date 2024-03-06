package by.klimov.commentservice;

import by.klimov.commentservice.container.PostgresSqlContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CommentServiceApplicationTests extends PostgresSqlContainerInitializer {

  @Test
  void contextLoads() {}
}
