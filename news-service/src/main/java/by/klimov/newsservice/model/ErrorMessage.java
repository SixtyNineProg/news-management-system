package by.klimov.newsservice.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {

  private int statusCode;
  private Date timestamp;
  private String message;
  private String description;
}
