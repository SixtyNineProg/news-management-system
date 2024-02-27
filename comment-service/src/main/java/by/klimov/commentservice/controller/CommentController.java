package by.klimov.commentservice.controller;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.exception.NotFoundException;
import by.klimov.commentservice.service.CommentService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
  public ResponseEntity<Object> save(@RequestBody CommentDto commentDto) {
    CommentDto serviceCommentDto = commentService.create(commentDto);
    return new ResponseEntity<>(serviceCommentDto, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<Object> get(
      @RequestParam(name = "page_number") Integer pageNumber,
      @RequestParam(name = "page_size", defaultValue = "15") Integer pageSize) {
    Page<CommentDto> commentDtoPage = commentService.readAll(PageRequest.of(pageNumber, pageSize));
    return ResponseEntity.ok(commentDtoPage);
  }

  @GetMapping("/search")
  public ResponseEntity<Object> search(
      @RequestParam String text,
      @RequestParam List<String> fields,
      @RequestParam(name = "page_number") Integer pageNumber,
      @RequestParam(name = "page_size", defaultValue = "15") Integer pageSize) {
    return ResponseEntity.ok(
        commentService.search(text, fields, PageRequest.of(pageNumber, pageSize)));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> getHouseById(@PathVariable Integer id) {
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
  public ResponseEntity<Object> deleteByUuid(@PathVariable Integer id) {
    commentService.deleteByUuid(id);
    return ResponseEntity.ok(id);
  }
}
