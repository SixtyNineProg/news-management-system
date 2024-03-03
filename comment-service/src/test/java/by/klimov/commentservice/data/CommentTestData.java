package by.klimov.commentservice.data;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.entity.Comment;
import by.klimov.commentservice.mapper.CommentMapper;
import by.klimov.commentservice.model.CommentsFilter;
import by.klimov.commentservice.specification.CommentSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@Builder(setterPrefix = "with", toBuilder = true)
@Accessors(chain = true)
public class CommentTestData {

  public static final long TEST_MILLIS = 1705953644075L;

  public static final String PARAM_NAME_PAGE_NUMBER = "page_number";
  public static final String PARAM_NAME_PAGE_SIZE = "page_size";

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

  private final ObjectMapper objectMapper = initObjectMapper();

  private final CommentMapper commentMapper = CommentMapper.INSTANCE;

  @Builder.Default
  private LocalDateTime timeLocalDateTime =
      LocalDateTime.ofInstant(Instant.ofEpochMilli(TEST_MILLIS), ZoneId.systemDefault());

  public Comment buildComment() {
    return Comment.builder().id(id).time(time).text(text).userName(userName).newsId(newsId).build();
  }

  public Comment buildCommentWithNewsIdTwo() {
    return Comment.builder().id(id).time(time).text(text).userName(userName).newsId(2).build();
  }

  public List<Comment> buildComments() {
    Comment comment = buildComment();
    Comment commentWithNewsIdTwo = buildCommentWithNewsIdTwo();
    return List.of(
        comment,
        comment,
        comment,
        commentWithNewsIdTwo,
        commentWithNewsIdTwo,
        commentWithNewsIdTwo);
  }

  public CommentDto buildCommentDto() {
    return commentMapper.toCommentDto(buildComment());
  }

  public List<CommentDto> buildCommentDtos() {
    return buildComments().stream().map(commentMapper::toCommentDto).toList();
  }

  public Page<CommentDto> buildCommentDtoPage() {
    return new PageImpl<>(buildCommentDtos(), PAGE_REQUEST, TOTAL);
  }

  public Page<Comment> buildCommentPage() {
    return new PageImpl<>(buildComments(), PAGE_REQUEST, TOTAL);
  }

  public String buildJsonCommentDto() throws JsonProcessingException {
    return objectMapper.writeValueAsString(buildCommentDto());
  }

  public CommentsFilter buildCommentFilterForId() {
    return new CommentsFilter(id);
  }

  public Specification<Comment> buildCommentSpecificationForId() {
    return new CommentSpecification().matchesFilter(buildCommentFilterForId());
  }

  private ObjectMapper initObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }

  public PageRequest buildPageRequest() {
    return PageRequest.of(CommentTestData.PAGE_NUMBER, CommentTestData.PAGE_SIZE);
  }

  public Optional<Comment> buildOptionalComment() {
    return Optional.of(buildComment());
  }
}
