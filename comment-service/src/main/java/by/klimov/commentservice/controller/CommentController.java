package by.klimov.commentservice.controller;

import static by.klimov.commentservice.util.Constants.MAX_PAGE_SIZE_LIMIT;
import static by.klimov.commentservice.util.Constants.MIN_PAGE_SIZE_LIMIT;
import static by.klimov.commentservice.util.Constants.PAGE_SIZE_LIMIT;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.exception.NotFoundException;
import by.klimov.commentservice.service.CommentService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
      @RequestParam @Min(0) Integer offset,
      @RequestParam(defaultValue = PAGE_SIZE_LIMIT)
          @Min(MIN_PAGE_SIZE_LIMIT)
          @Max(MAX_PAGE_SIZE_LIMIT)
          Integer limit) {
    Page<CommentDto> commentDtoPage = commentService.readAll(offset, limit);
    return ResponseEntity.ok(commentDtoPage);
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
