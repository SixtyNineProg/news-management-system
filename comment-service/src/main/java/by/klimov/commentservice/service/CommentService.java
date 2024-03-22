package by.klimov.commentservice.service;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.model.CommentsFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService extends CrudService<CommentDto, Integer> {

  Page<CommentDto> readAll(Pageable pageable);

  Page<CommentDto> search(String text, List<String> fields, Pageable pageable);

  Page<CommentDto> readAllWithFilter(CommentsFilter commentsFilter, Pageable pageable);

  void deleteAllWithFilter(CommentsFilter commentsFilter);
}
