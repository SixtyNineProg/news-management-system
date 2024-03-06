package by.klimov.newsservice;

import by.klimov.newsservice.container.PostgresSqlContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsServiceApplicationTests extends PostgresSqlContainerInitializer {

  @Test
  void contextLoads() {}
}
