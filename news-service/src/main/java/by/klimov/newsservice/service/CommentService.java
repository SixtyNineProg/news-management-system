package by.klimov.newsservice.service;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.model.CommentsFilter;
import java.util.List;
import org.springframework.data.domain.Page;

public interface CommentService {

  List<Page<CommentDto>> getAllCommentDtoPagesWithFilter(
      CommentsFilter commentsFilter, Integer commentDtoPageSize);

  void deleteAllWithFilter(CommentsFilter commentsFilter);
}
