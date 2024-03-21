package by.klimov.commentservice.controller;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.exception.NotFoundException;
import by.klimov.commentservice.model.CommentsFilter;
import by.klimov.commentservice.service.CommentService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping(
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<CommentDto> save(@RequestBody CommentDto commentDto) {
    CommentDto serviceCommentDto = commentService.create(commentDto);
    return new ResponseEntity<>(serviceCommentDto, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Page<CommentDto>> getAll(@PageableDefault(size = 15) Pageable pageable) {
    Page<CommentDto> commentDtoPage = commentService.readAll(pageable);
    return ResponseEntity.ok(commentDtoPage);
  }

  @PostMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<CommentDto>> getAllWithFilter(
      @PageableDefault(size = 15) Pageable pageable, @RequestBody CommentsFilter commentsFilter) {
    Page<CommentDto> commentDtoPage = commentService.readAllWithFilter(commentsFilter, pageable);
    return ResponseEntity.ok(commentDtoPage);
  }

  @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> deleteAllWithFilter(@RequestBody CommentsFilter commentsFilter) {
    commentService.deleteAllWithFilter(commentsFilter);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/search")
  public ResponseEntity<Page<CommentDto>> search(
      @RequestParam String text,
      @RequestParam List<String> fields,
      @PageableDefault(size = 15) Pageable pageable) {
    return ResponseEntity.ok(commentService.search(text, fields, pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CommentDto> getById(@PathVariable Integer id) {
    Optional<CommentDto> commentDto = commentService.readById(id);
    return ResponseEntity.ok(
        commentDto.orElseThrow(() -> new NotFoundException("Comment not found")));
  }

  @PutMapping(
      consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<Object> update(@RequestBody CommentDto commentDto) {
    return new ResponseEntity<>(commentService.update(commentDto), HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
    commentService.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
