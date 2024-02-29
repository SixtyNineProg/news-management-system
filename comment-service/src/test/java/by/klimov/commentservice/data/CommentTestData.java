package by.klimov.commentservice.data;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.entity.Comment;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Builder(setterPrefix = "with", toBuilder = true)
@Accessors(chain = true)
public class CommentTestData {

  public static final long TEST_MILLIS = 1705953644075L;
  public static final Integer PAGE_NUMBER = 1;
  public static final Integer PAGE_SIZE = 4;
  public static final long TOTAL = 5;
  public static final String SEARCHING_TEXT = "word";
  public static final PageRequest PAGE_REQUEST =
      PageRequest.of(CommentTestData.PAGE_NUMBER, CommentTestData.PAGE_SIZE);
  public static final String SEARCHABLE_FILED = "text";
  public static final List<String> SEARCHABLE_FILED_LIST =
      Collections.singletonList(SEARCHABLE_FILED);
  public static final String UNKNOWN_SEARCH_FIELD = "unknown_search_field";
  public static final List<String> UNKNOWN_SEARCHABLE_FILED_LIST =
      Collections.singletonList(UNKNOWN_SEARCH_FIELD);

  @Builder.Default private Integer id = 1;

  @Builder.Default private Timestamp time = new Timestamp(TEST_MILLIS);

  @Builder.Default private String text = "I am so excited about this.";

  @Builder.Default private String userName = "Jane";

  @Builder.Default private Integer newsId = 1;

  @Builder.Default
  private LocalDateTime timeLocalDateTime =
      LocalDateTime.ofInstant(Instant.ofEpochMilli(TEST_MILLIS), ZoneId.systemDefault());

  public Comment buildComment() {
    return Comment.builder().id(id).time(time).text(text).userName(userName).newsId(newsId).build();
  }

  public List<Comment> buildComments() {
    Comment comment = buildComment();
    return List.of(comment, comment, comment);
  }

  public CommentDto buildCommentDto() {
    return CommentDto.builder()
        .id(id)
        .time(timeLocalDateTime)
        .text(text)
        .userName(userName)
        .newsId(newsId)
        .build();
  }

  public List<CommentDto> buildCommentDtos() {
    CommentDto commentDto = buildCommentDto();
    return List.of(commentDto, commentDto, commentDto);
  }

  public Page<CommentDto> buildCommentDtoPage() {
    return new PageImpl<>(buildCommentDtos(), PAGE_REQUEST, TOTAL);
  }

  public Page<Comment> buildCommentPage() {
    return new PageImpl<>(buildComments(), PAGE_REQUEST, TOTAL);
  }
}
