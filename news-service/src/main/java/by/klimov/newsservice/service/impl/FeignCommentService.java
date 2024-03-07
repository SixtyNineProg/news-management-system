package by.klimov.newsservice.service.impl;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.exception.CommentServiceException;
import by.klimov.newsservice.feign.CommentFeign;
import by.klimov.newsservice.model.CommentsFilter;
import by.klimov.newsservice.service.CommentService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignCommentService implements CommentService {

  public static final String RESPONSE_ENTITY_IS_EMPTY =
      "An error occurred while executing the request. Response entity is empty";
  public static final String BAD_REQUEST_STATUS_CODE =
      "An error occurred while executing the request. Status code: ";

  private final CommentFeign commentFeign;

  /**
   * Retrieves all comments that match the provided filter in a paginated format.
   *
   * @param commentsFilter The filter to apply when retrieving the comments.
   * @param commentDtoPageSize The size of the page for comments.
   * @return A list of pages of CommentDto objects that match the filter.
   * @throws CommentServiceException If there is an issue with the response entity or if the
   *     response status is not OK.
   */
  @Override
  public List<Page<CommentDto>> getAllCommentDtoPagesWithFilter(
      CommentsFilter commentsFilter, Integer commentDtoPageSize) {
    List<Page<CommentDto>> commentDtos = new ArrayList<>();

    for (int page = 0; ; page++) {
      ResponseEntity<Page<CommentDto>> pageResponseEntity =
          commentFeign.getAllWithFilter(page, commentDtoPageSize, commentsFilter);

      if (Objects.isNull(pageResponseEntity)) {
        throw new CommentServiceException(RESPONSE_ENTITY_IS_EMPTY);
      }

      if (!pageResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
        throw new CommentServiceException(
            BAD_REQUEST_STATUS_CODE + pageResponseEntity.getStatusCode());
      }

      Page<CommentDto> commentDtoPage = pageResponseEntity.getBody();
      if (Objects.isNull(commentDtoPage)) {
        throw new CommentServiceException(
            "An error occurred while executing the request. Response entity body is empty");
      }

      commentDtos.add(commentDtoPage);

      if (!commentDtoPage.hasNext()) {
        break;
      }
    }
    return commentDtos;
  }

  /**
   * Deletes all comments that match the provided filter.
   *
   * @param commentsFilter The filter to apply when deleting the comments.
   * @throws CommentServiceException If there is an issue with the response entity or if the
   *     response status is not OK.
   */
  @Override
  public void deleteAllWithFilter(CommentsFilter commentsFilter) {
    ResponseEntity<Void> responseEntity = commentFeign.deleteAllWithFilter(commentsFilter);

    if (Objects.isNull(responseEntity)) {
      throw new CommentServiceException(RESPONSE_ENTITY_IS_EMPTY);
    }

    if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
      throw new CommentServiceException(BAD_REQUEST_STATUS_CODE + responseEntity.getStatusCode());
    }
  }

  /**
   * Retrieves a page of comments that match the provided filter.
   *
   * @param pageRequest The pagination and sorting details for the comments.
   * @param commentsFilter The filter to apply when retrieving the comments.
   * @return A paginated list of CommentDto objects that match the filter.
   * @throws CommentServiceException If there is an issue with the response entity or if the
   *     response status is not OK.
   */
  @Override
  public Page<CommentDto> getCommentDtoPageWithFilter(
      PageRequest pageRequest, CommentsFilter commentsFilter) {
    ResponseEntity<Page<CommentDto>> responseEntity =
        commentFeign.getAllWithFilter(
            pageRequest.getPageNumber(), pageRequest.getPageSize(), commentsFilter);

    if (Objects.isNull(responseEntity)) {
      throw new CommentServiceException(RESPONSE_ENTITY_IS_EMPTY);
    }

    if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
      throw new CommentServiceException(BAD_REQUEST_STATUS_CODE + responseEntity.getStatusCode());
    }

    return responseEntity.getBody();
  }
}
