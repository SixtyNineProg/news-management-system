package by.klimov.newsservice.service;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.dto.NewsDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface NewsService extends CrudService<NewsDto, Integer> {

  Page<NewsDto> readAll(PageRequest pageRequest, Integer commentsPageSize);

  Page<NewsDto> search(
      String text, List<String> fields, PageRequest pageRequest, Integer commentsPageSize);

  Page<CommentDto> readCommentsByNewsId(Integer newsId, PageRequest pageRequest);

  CommentDto readCommentByIdFromNews(Integer newsId, Integer commentId);
}
