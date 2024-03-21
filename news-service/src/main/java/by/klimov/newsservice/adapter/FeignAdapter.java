package by.klimov.newsservice.adapter;

import by.klimov.newsservice.exception.CommentServiceException;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FeignAdapter {

  public static final String RESPONSE_ENTITY_IS_EMPTY =
      "An error occurred while executing the request. Response entity is empty";
  public static final String BAD_REQUEST_STATUS_CODE =
      "An error occurred while executing the request. Status code: ";

  public <T> T getBody(ResponseEntity<T> responseEntity) {
    if (Objects.isNull(responseEntity)) {
      throw new CommentServiceException(RESPONSE_ENTITY_IS_EMPTY);
    }

    if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
      throw new CommentServiceException(BAD_REQUEST_STATUS_CODE + responseEntity.getStatusCode());
    }

    T body = responseEntity.getBody();
    if (Objects.isNull(body)) {
      throw new CommentServiceException(
          "An error occurred while executing the request. Response entity body is empty");
    }

    return body;
  }
}
