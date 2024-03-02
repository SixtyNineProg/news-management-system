package by.klimov.commentservice.service;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.model.CommentsFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CommentService extends CrudService<CommentDto, Integer> {

  Page<CommentDto> readAll(PageRequest pageRequest);

  Page<CommentDto> search(String text, List<String> fields, PageRequest pageRequest);

  Page<CommentDto> readAllWithFilter(CommentsFilter commentsFilter, PageRequest pageRequest);
}
