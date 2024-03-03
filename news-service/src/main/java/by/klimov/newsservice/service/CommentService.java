package by.klimov.newsservice.service;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.model.CommentsFilter;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

  List<Page<CommentDto>> getAllCommentDtoPagesWithFilter(CommentsFilter commentsFilter, Integer commentDtoPageSize);
}
