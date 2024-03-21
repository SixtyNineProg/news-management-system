package by.klimov.newsservice.service;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.dto.NewsDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsService extends CrudService<NewsDto, Integer> {

  Page<NewsDto> readAll(Pageable pageable, Integer commentsPageSize);

  Page<NewsDto> search(
          String text, List<String> fields, Pageable pageable, Integer commentsPageSize);

  Page<CommentDto> readCommentsByNewsId(Integer newsId, Pageable pageable);

  CommentDto readCommentByIdFromNews(Integer newsId, Integer commentId);
}
