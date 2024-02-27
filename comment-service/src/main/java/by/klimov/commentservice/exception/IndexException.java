package by.klimov.commentservice.exception;

public class IndexException extends RuntimeException {

  public IndexException(String errorMessage, Exception e) {
    super(errorMessage, e);
  }
}
