package by.klimov.newsservice.controller;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.dto.NewsDto;
import by.klimov.newsservice.service.NewsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

  private final NewsService newsService;

  @PostMapping(
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<NewsDto> save(@RequestBody NewsDto newsDto) {
    NewsDto serviceNewsDto = newsService.create(newsDto);
    return new ResponseEntity<>(serviceNewsDto, HttpStatus.CREATED);
  }

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Page<NewsDto>> getAll(
      @RequestParam(name = "page_number") Integer pageNumber,
      @RequestParam(name = "page_size", defaultValue = "15", required = false) Integer pageSize,
      @RequestParam(name = "comments_page_size", defaultValue = "15", required = false)
          Integer commentsPageSize) {
    Page<NewsDto> newsDtoPage =
        newsService.readAll(PageRequest.of(pageNumber, pageSize), commentsPageSize);
    return ResponseEntity.ok(newsDtoPage);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<NewsDto>> search(
      @RequestParam String text,
      @RequestParam List<String> fields,
      @RequestParam(name = "page_number") Integer pageNumber,
      @RequestParam(name = "page_size", defaultValue = "15") Integer pageSize,
      @RequestParam(name = "comments_page_size", defaultValue = "15", required = false)
          Integer commentsPageSize) {
    return ResponseEntity.ok(
        newsService.search(text, fields, PageRequest.of(pageNumber, pageSize), commentsPageSize));
  }

  @GetMapping("/{id}")
  public ResponseEntity<NewsDto> getById(
      @PathVariable Integer id,
      @RequestParam(name = "comments_page_size", defaultValue = "15", required = false)
          Integer commentsPageSize) {
    NewsDto newsDto = newsService.readById(id, commentsPageSize);
    return ResponseEntity.ok(newsDto);
  }

  @GetMapping("/{id}/comments")
  public ResponseEntity<Page<CommentDto>> getCommentsByNewsId(
      @PathVariable Integer id,
      @RequestParam(name = "page_number") Integer pageNumber,
      @RequestParam(name = "page_size", defaultValue = "15") Integer pageSize) {
    Page<CommentDto> commentDtoPage =
        newsService.readCommentsByNewsId(id, PageRequest.of(pageNumber, pageSize));
    return ResponseEntity.ok(commentDtoPage);
  }

  @GetMapping("/{newsId}/comments/{commentId}")
  public ResponseEntity<CommentDto> getCommentsByIdFromNews(
      @PathVariable Integer newsId, @PathVariable Integer commentId) {
    CommentDto commentDto = newsService.readCommentByIdFromNews(newsId, commentId);
    return ResponseEntity.ok(commentDto);
  }

  @PutMapping(
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Object> update(@RequestBody NewsDto newsDto) {
    return new ResponseEntity<>(newsService.update(newsDto), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
    newsService.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
