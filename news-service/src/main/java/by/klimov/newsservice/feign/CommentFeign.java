package by.klimov.newsservice.feign;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.model.CommentsFilter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "${feign.client.comment-service.name}",
    url = "${feign.client.comment-service.url}")
public interface CommentFeign {

  @PostMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Page<CommentDto>> getAllWithFilter(
      @RequestParam(name = "page_number") Integer pageNumber,
      @RequestParam(name = "page_size", defaultValue = "15", required = false) Integer pageSize,
      @RequestBody CommentsFilter commentsFilter);

  @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<Void> deleteAllWithFilter(@RequestBody CommentsFilter commentsFilter);
}
