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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeignCommentService implements CommentService {

  private final CommentFeign commentFeign;

  @Override
  public List<Page<CommentDto>> getAllCommentDtoPagesWithFilter(
      CommentsFilter commentsFilter, Integer commentDtoPageSize) {
    List<Page<CommentDto>> commentDtos = new ArrayList<>();

    for (int page = 0; ; page++) {
      ResponseEntity<Page<CommentDto>> pageResponseEntity =
          commentFeign.getAllWithFilter(page, commentDtoPageSize, commentsFilter);

      if (Objects.isNull(pageResponseEntity)) {
        throw new CommentServiceException(
            "An error occurred while executing the request. Response entity is empty");
      }

      if (!pageResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
        throw new CommentServiceException(
            "An error occurred while executing the request. Status code: "
                + pageResponseEntity.getStatusCode());
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

  @Override
  public void deleteAllWithFilter(CommentsFilter commentsFilter) {
    ResponseEntity<Void> responseEntity = commentFeign.deleteAllWithFilter(commentsFilter);

    if (Objects.isNull(responseEntity)) {
      throw new CommentServiceException(
          "An error occurred while executing the request. Response entity is empty");
    }

    if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
      throw new CommentServiceException(
          "An error occurred while executing the request. Status code: "
              + responseEntity.getStatusCode());
    }
  }
}
