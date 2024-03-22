package by.klimov.commentservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI usersMicroserviceOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Comment API")
                .description("Service for managing comments")
                .version("1.0.0"));
  }
}
