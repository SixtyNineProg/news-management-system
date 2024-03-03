package by.klimov.newsservice.exception;

public class NotFoundException extends RuntimeException {

  public NotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
