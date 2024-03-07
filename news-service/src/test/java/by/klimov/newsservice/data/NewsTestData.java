package by.klimov.newsservice.data;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.dto.NewsDto;
import by.klimov.newsservice.entity.News;
import by.klimov.newsservice.mapper.NewsMapper;
import by.klimov.newsservice.model.CommentsFilter;
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

@Builder(setterPrefix = "with", toBuilder = true)
@Accessors(chain = true)
public class NewsTestData {

  public static final long TEST_MILLIS = 1705953644075L;

  public static final String PARAM_NAME_PAGE_NUMBER = "page_number";
  public static final String PARAM_NAME_PAGE_SIZE = "page_size";

  public static final Integer PAGE_NUMBER = 1;
  public static final Integer PAGE_SIZE = 4;
  public static final long TOTAL = 5;
  public static final PageRequest PAGE_REQUEST =
      PageRequest.of(NewsTestData.PAGE_NUMBER, NewsTestData.PAGE_SIZE);
  public static final int COMMENTS_PAGE_SIZE = 10;

  public static final int UNKNOWN_COMMENT_ID = 135;

  public static final String SEARCH_TEXT = "search";
  public static final String SEARCHING_TEXT = "word";
  public static final String SEARCHABLE_FILED = "text";
  public static final String INVALID_SEARCHABLE_FILED = "invalid";
  public static final List<String> SEARCHABLE_FILED_LIST =
      Collections.singletonList(SEARCHABLE_FILED);
  public static final List<String> INVALID_SEARCHABLE_FILED_LIST =
      Collections.singletonList(INVALID_SEARCHABLE_FILED);
  public static final String UNKNOWN_SEARCH_FIELD = "unknown_search_field";
  public static final List<String> UNKNOWN_SEARCHABLE_FILED_LIST =
      Collections.singletonList(UNKNOWN_SEARCH_FIELD);

  public static final String PARAM_NAME_COMMENTS_PAGE_SIZE = "comments_page_size";
  public static final String PARAM_NAME_TEXT = "text";
  public static final String PARAM_NAME_FIELDS = "fields";

  private final ObjectMapper objectMapper = initObjectMapper();
  private final NewsMapper newsMapper = NewsMapper.INSTANCE;

  @Builder.Default
  List<Page<CommentDto>> comments = CommentTestData.builder().build().buildListCommentDtoPage();

  @Builder.Default private Integer id = 1;

  @Builder.Default private Timestamp time = new Timestamp(TEST_MILLIS);

  @Builder.Default private String title = "Future Tech: New Horizons";

  @Builder.Default
  private String text =
      "An overview of the latest achievements in technology and their potential impact on our future.";

  @Builder.Default
  private LocalDateTime timeLocalDateTime =
      LocalDateTime.ofInstant(Instant.ofEpochMilli(TEST_MILLIS), ZoneId.systemDefault());

  public News buildNews() {
    return News.builder().id(id).time(time).title(title).text(text).build();
  }

  public List<News> buildNewsList() {
    News News = buildNews();
    return List.of(News, News, News);
  }

  public NewsDto buildNewsDto() {
    return newsMapper.toDto(buildNews());
  }

  public List<NewsDto> buildNewsDtos() {
    return buildNewsList().stream().map(newsMapper::toDto).toList();
  }

  public Page<NewsDto> buildNewsDtoPage() {
    return new PageImpl<>(buildNewsDtos(), PAGE_REQUEST, TOTAL);
  }

  public Page<News> buildNewsPage() {
    return new PageImpl<>(buildNewsList(), PAGE_REQUEST, TOTAL);
  }

  public String buildJsonNewsDto() throws JsonProcessingException {
    return objectMapper.writeValueAsString(buildNewsDto());
  }

  public PageRequest buildPageRequest() {
    return PageRequest.of(NewsTestData.PAGE_NUMBER, NewsTestData.PAGE_SIZE);
  }

  public Optional<News> buildOptionalNews() {
    return Optional.of(buildNews());
  }

  public CommentsFilter buildCommentFilterForId() {
    return new CommentsFilter(id);
  }

  public NewsDto buildNewsDtoWithComments() {
    NewsDto newsDto = newsMapper.toDto(buildNews());
    newsDto.setComments(comments);
    return newsDto;
  }

  public List<NewsDto> buildNewsDtosWithComments() {
    return buildNewsList().stream()
        .map(newsMapper::toDto)
        .peek(newsDto -> newsDto.setComments(comments))
        .toList();
  }

  public Page<NewsDto> buildNewsDtoWithCommentsPage() {
    return new PageImpl<>(buildNewsDtosWithComments(), PAGE_REQUEST, TOTAL);
  }

  private ObjectMapper initObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }
}
