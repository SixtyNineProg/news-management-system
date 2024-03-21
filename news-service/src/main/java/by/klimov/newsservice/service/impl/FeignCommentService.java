package by.klimov.newsservice.service.impl;

import by.klimov.newsservice.adapter.FeignAdapter;
import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.exception.CommentServiceException;
import by.klimov.newsservice.feign.CommentFeign;
import by.klimov.newsservice.model.CommentsFilter;
import by.klimov.newsservice.service.CommentService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignCommentService implements CommentService {

  private final CommentFeign commentFeign;
  private final FeignAdapter feignAdapter;

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

      Page<CommentDto> commentDtoPage = feignAdapter.getBody(pageResponseEntity);
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

    feignAdapter.getBody(responseEntity);
  }

  /**
   * Retrieves a page of comments that match the provided filter.
   *
   * @param pageable The pagination and sorting details for the comments.
   * @param commentsFilter The filter to apply when retrieving the comments.
   * @return A paginated list of CommentDto objects that match the filter.
   * @throws CommentServiceException If there is an issue with the response entity or if the
   *     response status is not OK.
   */
  @Override
  public Page<CommentDto> getCommentDtoPageWithFilter(
      Pageable pageable, CommentsFilter commentsFilter) {

    ResponseEntity<Page<CommentDto>> responseEntity =
        commentFeign.getAllWithFilter(
            pageable.getPageNumber(), pageable.getPageSize(), commentsFilter);

    return feignAdapter.getBody(responseEntity);
  }
}
