package by.klimov.newsservice.controller;

import by.klimov.newsservice.dto.NewsDto;
import by.klimov.newsservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

  private final NewsService newsService;

  //  @PostMapping(
  //      consumes = {MediaType.APPLICATION_JSON_VALUE},
  //      produces = {MediaType.APPLICATION_JSON_VALUE})
  //  public ResponseEntity<Object> save(@RequestBody CommentDto commentDto) {
  //    CommentDto serviceCommentDto = newsService.create(commentDto);
  //    return new ResponseEntity<>(serviceCommentDto, HttpStatus.CREATED);
  //  }
  //
  //  @GetMapping
  //  public ResponseEntity<Object> getAll(
  //      @RequestParam(name = "page_number") Integer pageNumber,
  //      @RequestParam(name = "page_size", defaultValue = "15") Integer pageSize) {
  //    Page<CommentDto> commentDtoPage = newsService.readAll(PageRequest.of(pageNumber, pageSize));
  //    return ResponseEntity.ok(commentDtoPage);
  //  }
  //
  //  @PostMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
  //  public ResponseEntity<Object> getAllByWithFilter(
  //      @RequestParam(name = "page_number") Integer pageNumber,
  //      @RequestParam(name = "page_size", defaultValue = "15", required = false) Integer pageSize,
  //      @RequestBody CommentsFilter commentsFilter) {
  //    Page<CommentDto> commentDtoPage =
  //        newsService.readAllWithFilter(commentsFilter, PageRequest.of(pageNumber, pageSize));
  //    return ResponseEntity.ok(commentDtoPage);
  //  }
  //
  //  @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  //  public ResponseEntity<Void> deleteAllWithFilter(@RequestBody CommentsFilter commentsFilter) {
  //    newsService.deleteAllWithFilter(commentsFilter);
  //    return ResponseEntity.ok().build();
  //  }
  //
  //  @GetMapping("/search")
  //  public ResponseEntity<Object> search(
  //      @RequestParam String text,
  //      @RequestParam List<String> fields,
  //      @RequestParam(name = "page_number") Integer pageNumber,
  //      @RequestParam(name = "page_size", defaultValue = "15") Integer pageSize) {
  //    return ResponseEntity.ok(
  //        newsService.search(text, fields, PageRequest.of(pageNumber, pageSize)));
  //  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getById(
      @PathVariable Integer id,
      @RequestParam(name = "comments_page_size", defaultValue = "15", required = false)
          Integer commentsPageSize) {
    NewsDto newsDto = newsService.readById(id, commentsPageSize);
    return ResponseEntity.ok(newsDto);
  }

  //  @PutMapping(
  //      consumes = {MediaType.APPLICATION_JSON_VALUE},
  //      produces = {MediaType.APPLICATION_JSON_VALUE})
  //  public ResponseEntity<Object> update(@RequestBody CommentDto commentDto) {
  //    return new ResponseEntity<>(newsService.update(commentDto), HttpStatus.CREATED);
  //  }
  //
  //  @DeleteMapping("/{id}")
  //  public ResponseEntity<Object> deleteById(@PathVariable Integer id) {
  //    newsService.deleteById(id);
  //    return ResponseEntity.ok(id);
  //  }
}
