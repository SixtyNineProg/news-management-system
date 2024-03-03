package by.klimov.newsservice.exception;

public class CommentServiceException extends RuntimeException {

  public CommentServiceException(String errorMessage) {
    super(errorMessage);
  }
}
