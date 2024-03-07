package by.klimov.newsservice.service.impl;

import static by.klimov.newsservice.data.NewsTestData.COMMENTS_PAGE_SIZE;
import static by.klimov.newsservice.data.NewsTestData.INVALID_SEARCHABLE_FILED_LIST;
import static by.klimov.newsservice.data.NewsTestData.PAGE_REQUEST;
import static by.klimov.newsservice.data.NewsTestData.SEARCHABLE_FILED_LIST;
import static by.klimov.newsservice.data.NewsTestData.SEARCHING_TEXT;
import static by.klimov.newsservice.data.NewsTestData.UNKNOWN_COMMENT_ID;
import static by.klimov.newsservice.service.impl.NewsServiceImpl.ERROR_FORMAT_COMMENT_NEWS_NOT_FOUND;
import static by.klimov.newsservice.service.impl.NewsServiceImpl.ERROR_FORMAT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import by.klimov.newsservice.data.CommentTestData;
import by.klimov.newsservice.data.NewsTestData;
import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.dto.NewsDto;
import by.klimov.newsservice.entity.News;
import by.klimov.newsservice.exception.NotFoundException;
import by.klimov.newsservice.mapper.NewsMapper;
import by.klimov.newsservice.model.CommentsFilter;
import by.klimov.newsservice.repository.NewsRepository;
import by.klimov.newsservice.service.CommentService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@SpringBootTest(classes = NewsServiceImpl.class)
class NewsServiceImplTest {

  @MockBean private NewsRepository newsRepository;

  @MockBean private NewsMapper newsMapper;

  @MockBean private CommentService commentService;

  @Autowired private NewsServiceImpl newsService;

  @Captor private ArgumentCaptor<News> newsArgumentCaptor;
  @Captor private ArgumentCaptor<CommentsFilter> commentsFilterArgumentCaptor;

  @Test
  void create_whenCreate_thenEntityExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    NewsDto expected = newsTestData.buildNewsDtoWithComments();
    News news = newsTestData.buildNews();
    News newsWithoutId = NewsTestData.builder().withId(null).build().buildNews();

    doReturn(newsWithoutId).when(newsMapper).toEntity(expected);
    doReturn(news).when(newsRepository).save(newsWithoutId);
    doReturn(expected).when(newsMapper).toDto(news);

    // when
    NewsDto actual = newsService.create(expected);

    // then
    assertThat(actual)
        .isNotNull()
        .hasNoNullFieldsOrPropertiesExcept(NewsDto.Fields.comments)
        .hasFieldOrPropertyWithValue(NewsDto.Fields.time, expected.getTime())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.title, expected.getTitle())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.text, expected.getText());
  }

  @Test
  void readById_whenGetById_thenOptionalDtoExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    CommentTestData commentTestData = CommentTestData.builder().build();
    NewsDto expected = newsTestData.buildNewsDtoWithComments();
    News news = newsTestData.buildNews();
    Optional<News> optionalNews = newsTestData.buildOptionalNews();

    doReturn(optionalNews).when(newsRepository).findById(expected.getId());
    doReturn(expected).when(newsMapper).toDto(news);
    doReturn(commentTestData.buildListCommentDtoPage())
        .when(commentService)
        .getAllCommentDtoPagesWithFilter(
            newsTestData.buildCommentFilterForId(), COMMENTS_PAGE_SIZE);

    // when
    NewsDto actual = newsService.readById(expected.getId(), COMMENTS_PAGE_SIZE);

    // then
    assertThat(actual)
        .hasFieldOrPropertyWithValue(NewsDto.Fields.id, expected.getId())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.time, expected.getTime())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.title, expected.getTitle())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.text, expected.getText())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.comments, expected.getComments());
  }

  @Test
  void readById_whenReadByIdDtoWithUnknownId_thenNotFoundExceptionExpected() {
    // given
    NewsDto expected = NewsTestData.builder().build().buildNewsDtoWithComments();
    Integer id = expected.getId();

    doReturn(Optional.empty()).when(newsRepository).findById(expected.getId());

    // when
    NotFoundException thrown =
        assertThrows(NotFoundException.class, () -> newsService.readById(id, COMMENTS_PAGE_SIZE));

    // then
    assertThat(thrown).hasMessage(String.format(ERROR_FORMAT_NOT_FOUND, expected.getId()));
  }

  @Test
  void update_whenUpdateDto_theUpdatedDtoExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    NewsDto expected = newsTestData.buildNewsDtoWithComments();
    News news = newsTestData.buildNews();
    Optional<News> optionalNews = newsTestData.buildOptionalNews();
    Integer id = expected.getId();

    doReturn(optionalNews).when(newsRepository).findById(id);
    doReturn(news).when(newsMapper).merge(expected, news);
    doReturn(news).when(newsRepository).save(news);
    doReturn(expected).when(newsMapper).toDto(news);

    // when
    NewsDto actual = newsService.update(expected);

    // then
    assertThat(actual)
        .hasFieldOrPropertyWithValue(NewsDto.Fields.id, expected.getId())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.time, expected.getTime())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.title, expected.getTitle())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.text, expected.getText())
        .hasFieldOrPropertyWithValue(NewsDto.Fields.comments, expected.getComments());
  }

  @Test
  void update_whenUpdateDtoWithUnknownId_thenNotFoundExceptionExpected() {
    // given
    NewsDto expected = NewsTestData.builder().build().buildNewsDtoWithComments();

    doReturn(Optional.empty()).when(newsRepository).findById(expected.getId());

    // when
    NotFoundException thrown =
        assertThrows(NotFoundException.class, () -> newsService.update(expected));

    // then
    assertThat(thrown).hasMessage(String.format(ERROR_FORMAT_NOT_FOUND, expected.getId()));
  }

  @Test
  void deleteById_whenDeleteById_thenCallRepositoryDeleteWithExpectedObject() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    Integer id = newsTestData.buildNewsDtoWithComments().getId();
    News news = newsTestData.buildNews();
    Optional<News> optionalNews = newsTestData.buildOptionalNews();

    doReturn(optionalNews).when(newsRepository).findById(id);
    doNothing().when(newsRepository).delete(news);
    doNothing().when(commentService).deleteAllWithFilter(newsTestData.buildCommentFilterForId());

    // when
    newsService.deleteById(id);

    // then
    verify(newsRepository).delete(newsArgumentCaptor.capture());
    assertThat(newsArgumentCaptor.getValue())
        .isNotNull()
        .hasFieldOrPropertyWithValue(News.Fields.id, news.getId())
        .hasFieldOrPropertyWithValue(News.Fields.time, news.getTime())
        .hasFieldOrPropertyWithValue(News.Fields.time, news.getTime())
        .hasFieldOrPropertyWithValue(News.Fields.text, news.getText());
  }

  @Test
  void deleteById_whenDeleteById_thenCallCommentRepositoryDeleteAllWithExpectedObject() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    Integer id = newsTestData.buildNewsDtoWithComments().getId();
    News news = newsTestData.buildNews();
    Optional<News> optionalNews = newsTestData.buildOptionalNews();
    CommentsFilter expected = newsTestData.buildCommentFilterForId();

    doReturn(optionalNews).when(newsRepository).findById(id);
    doNothing().when(newsRepository).delete(news);
    doNothing().when(commentService).deleteAllWithFilter(newsTestData.buildCommentFilterForId());

    // when
    newsService.deleteById(id);

    // then
    verify(commentService).deleteAllWithFilter(commentsFilterArgumentCaptor.capture());
    assertThat(commentsFilterArgumentCaptor.getValue()).isNotNull().isEqualTo(expected);
  }

  @Test
  void deleteById_whenDeleteWithUnknownId_thenNotFoundExceptionExpected() {
    // given
    Integer id = NewsTestData.builder().build().buildNewsDtoWithComments().getId();

    doReturn(Optional.empty()).when(newsRepository).findById(id);

    // when
    NotFoundException thrown =
        assertThrows(NotFoundException.class, () -> newsService.deleteById(id));

    // then
    assertThat(thrown).hasMessage(String.format(ERROR_FORMAT_NOT_FOUND, id));
  }

  @Test
  void readAll_whenReadAllWithPageRequest_thenValidPageExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    Page<NewsDto> expected = newsTestData.buildNewsDtoWithCommentsPage();
    Page<News> newsPage = newsTestData.buildNewsPage();
    News news = newsTestData.buildNews();
    NewsDto newsDto = newsTestData.buildNewsDto();
    CommentsFilter commentsFilter = newsTestData.buildCommentFilterForId();
    List<Page<CommentDto>> commetsList =
        CommentTestData.builder().build().buildListCommentDtoPage();

    doReturn(newsPage).when(newsRepository).findAll(expected.getPageable());
    doReturn(newsDto).when(newsMapper).toDto(news);
    doReturn(commetsList)
        .when(commentService)
        .getAllCommentDtoPagesWithFilter(commentsFilter, COMMENTS_PAGE_SIZE);

    // when
    Page<NewsDto> actual = newsService.readAll(PAGE_REQUEST, COMMENTS_PAGE_SIZE);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void readCommentsByNewsId_whenReadCommentsByNewsId_thenValidPageExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    Integer id = NewsTestData.builder().build().buildNewsDto().getId();
    PageRequest pageRequest = newsTestData.buildPageRequest();
    Page<CommentDto> commetscommentDtoPage =
        CommentTestData.builder().build().buildCommentDtoPage();

    doReturn(commetscommentDtoPage)
        .when(commentService)
        .getCommentDtoPageWithFilter(
            newsTestData.buildPageRequest(), newsTestData.buildCommentFilterForId());

    // when
    Page<CommentDto> actual = newsService.readCommentsByNewsId(id, pageRequest);

    // then
    assertThat(actual).isEqualTo(commetscommentDtoPage);
  }

  @Test
  void readCommentByIdFromNews_whenReadCommentByIdFromNews_thenValidObjectExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentDto expected = commentTestData.buildCommentDto();
    List<Page<CommentDto>> commetsPageList = commentTestData.buildListCommentDtoPage();
    Integer commentId = expected.id();
    Integer newsId = newsTestData.buildNewsDto().getId();
    CommentsFilter commentsFilter = newsTestData.buildCommentFilterForId();

    doReturn(commetsPageList)
        .when(commentService)
        .getAllCommentDtoPagesWithFilter(commentsFilter, COMMENTS_PAGE_SIZE);

    // when
    CommentDto actual = newsService.readCommentByIdFromNews(newsId, commentId);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void
      readCommentByIdFromNews_whenReadCommentByIdFromNewsWithUnknownId_thenNotFoundExceptionExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = newsTestData.buildCommentFilterForId();
    List<Page<CommentDto>> commetsPageList = commentTestData.buildListCommentDtoPage();
    Integer newsId = newsTestData.buildNewsDto().getId();
    Integer commentId = UNKNOWN_COMMENT_ID;

    doReturn(commetsPageList)
        .when(commentService)
        .getAllCommentDtoPagesWithFilter(commentsFilter, COMMENTS_PAGE_SIZE);

    // when
    NotFoundException thrown =
        assertThrows(
            NotFoundException.class, () -> newsService.readCommentByIdFromNews(newsId, commentId));

    // then
    assertThat(thrown)
        .hasMessage(String.format(ERROR_FORMAT_COMMENT_NEWS_NOT_FOUND, commentId, newsId));
  }

  @Test
  void search_whenSearchWithExistTextAndKnownFields_thenValidPageExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    NewsDto newsDto = newsTestData.buildNewsDto();
    Page<NewsDto> expected = newsTestData.buildNewsDtoWithCommentsPage();
    Page<News> newsPage = newsTestData.buildNewsPage();
    PageRequest pageRequest = newsTestData.buildPageRequest();
    News news = newsTestData.buildNews();
    CommentsFilter commentsFilter = newsTestData.buildCommentFilterForId();

    CommentTestData commentTestData = CommentTestData.builder().build();
    List<Page<CommentDto>> commetsPageList = commentTestData.buildListCommentDtoPage();

    doReturn(newsPage)
        .when(newsRepository)
        .searchBy(SEARCHING_TEXT, pageRequest, SEARCHABLE_FILED_LIST.toArray(new String[0]));
    doReturn(newsDto).when(newsMapper).toDto(news);
    doReturn(commetsPageList)
        .when(commentService)
        .getAllCommentDtoPagesWithFilter(commentsFilter, COMMENTS_PAGE_SIZE);

    // when
    Page<NewsDto> actual =
        newsService.search(SEARCHING_TEXT, SEARCHABLE_FILED_LIST, pageRequest, COMMENTS_PAGE_SIZE);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void search_whenSearchWithInvalidSearchField_thenIllegalArgumentExceptionExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    PageRequest pageRequest = newsTestData.buildPageRequest();

    // when
    assertThrows(
        IllegalArgumentException.class,
        () ->
            newsService.search(
                SEARCHING_TEXT, INVALID_SEARCHABLE_FILED_LIST, pageRequest, COMMENTS_PAGE_SIZE));

    // then
    // asserts in bock "when"
  }
}
