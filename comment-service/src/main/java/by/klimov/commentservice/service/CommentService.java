package by.klimov.commentservice.service;

import by.klimov.commentservice.dto.CommentDto;
import org.springframework.data.domain.Page;

public interface CommentService extends CrudService<CommentDto, Integer> {

  Page<CommentDto> readAll(Integer offset, Integer limit);
}
