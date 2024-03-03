package by.klimov.commentservice.service.impl;

import static by.klimov.commentservice.data.CommentTestData.PAGE_REQUEST;
import static by.klimov.commentservice.data.CommentTestData.SEARCHABLE_FILED;
import static by.klimov.commentservice.data.CommentTestData.SEARCHABLE_FILED_LIST;
import static by.klimov.commentservice.data.CommentTestData.SEARCHING_TEXT;
import static by.klimov.commentservice.data.CommentTestData.UNKNOWN_SEARCHABLE_FILED_LIST;
import static by.klimov.commentservice.service.impl.CommentServiceImpl.ERROR_FORMAT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import by.klimov.commentservice.data.CommentTestData;
import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.entity.Comment;
import by.klimov.commentservice.exception.NotFoundException;
import by.klimov.commentservice.mapper.CommentMapper;
import by.klimov.commentservice.model.CommentsFilter;
import by.klimov.commentservice.repository.CommentRepository;
import by.klimov.commentservice.specification.CommentSpecification;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
@SpringBootTest(classes = CommentServiceImpl.class)
class CommentServiceImplTest {

  @MockBean private final CommentRepository commentRepository;

  @MockBean private final CommentMapper commentMapper;

  @MockBean private final CommentSpecification commentSpecification;

  private final CommentServiceImpl commentService;

  @Captor private ArgumentCaptor<Comment> commentArgumentCaptor;

  @Captor private ArgumentCaptor<Specification<Comment>> specificationArgumentCaptor;

  @Captor private ArgumentCaptor<List<Comment>> listArgumentCaptor;

  @Test
  void create_whenCreateComment_thenCommentExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentDto expected = commentTestData.buildCommentDto();
    Comment comment = commentTestData.buildComment();
    CommentDto commentDto = CommentTestData.builder().build().buildCommentDto();
    Comment commentWithoutId = CommentTestData.builder().withId(null).build().buildComment();

    doReturn(comment).when(commentMapper).toComment(commentDto);
    doReturn(commentDto).when(commentMapper).toCommentDto(comment);
    doReturn(comment).when(commentRepository).save(commentWithoutId);

    // when
    CommentDto actual = commentService.create(commentDto);

    // then
    assertThat(actual)
        .hasFieldOrPropertyWithValue(CommentDto.Fields.userName, expected.userName())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.newsId, expected.newsId())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.text, expected.text())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.time, expected.time());
  }

  @Test
  void readById_whenGetById_thenOptionalCommentDtoExpected() {
    // given
    CommentDto expected = CommentTestData.builder().build().buildCommentDto();
    Comment Comment = CommentTestData.builder().build().buildComment();
    Optional<Comment> optionalComment = Optional.of(Comment);

    doReturn(optionalComment).when(commentRepository).findById(expected.id());
    doReturn(expected).when(commentMapper).toCommentDto(Comment);

    // when
    Optional<CommentDto> actual = commentService.readById(expected.id());

    // then
    assertThat(actual)
        .get()
        .hasFieldOrPropertyWithValue(CommentDto.Fields.id, expected.id())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.time, expected.time())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.text, expected.text())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.userName, expected.userName())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.newsId, expected.newsId());
  }

  @Test
  void update_whenUpdateCommentDto_theUpdatedCommentDtoExpected() {
    // given
    CommentDto expected = CommentTestData.builder().build().buildCommentDto();
    Comment Comment = CommentTestData.builder().build().buildComment();
    Optional<Comment> optionalComment = Optional.of(Comment);

    doReturn(optionalComment).when(commentRepository).findById(expected.id());
    doReturn(Comment).when(commentMapper).merge(expected, Comment);
    doReturn(Comment).when(commentRepository).save(Comment);

    // when
    CommentDto actual = commentService.update(expected);

    // then
    assertThat(actual)
        .hasFieldOrPropertyWithValue(CommentDto.Fields.id, expected.id())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.userName, expected.userName())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.time, expected.time())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.text, expected.text())
        .hasFieldOrPropertyWithValue(CommentDto.Fields.newsId, expected.newsId());
  }

  @Test
  void update_whenUpdateCommentDtoWithUnknownId_thenNotFoundExceptionExpected() {
    // given
    CommentDto expected = CommentTestData.builder().build().buildCommentDto();

    doReturn(Optional.empty()).when(commentRepository).findById(expected.id());

    // when
    NotFoundException thrown =
        assertThrows(NotFoundException.class, () -> commentService.update(expected));

    // then
    assertThat(thrown).hasMessage(String.format(ERROR_FORMAT_NOT_FOUND, expected.id()));
  }

  @Test
  void deleteById_whenDeleteById_thenCallRepositoryDeleteWithExpectedObject() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    Integer id = commentTestData.buildCommentDto().id();
    Comment comment = CommentTestData.builder().build().buildComment();
    Optional<Comment> optionalComment = commentTestData.buildOptionalComment();

    doNothing().when(commentRepository).delete(comment);
    doReturn(optionalComment).when(commentRepository).findById(id);

    // when
    commentService.deleteById(id);

    // then
    verify(commentRepository).delete(commentArgumentCaptor.capture());
    assertThat(commentArgumentCaptor.getValue())
        .isNotNull()
        .hasFieldOrPropertyWithValue(Comment.Fields.id, comment.getId())
        .hasFieldOrPropertyWithValue(Comment.Fields.userName, comment.getUserName())
        .hasFieldOrPropertyWithValue(Comment.Fields.time, comment.getTime())
        .hasFieldOrPropertyWithValue(Comment.Fields.text, comment.getText())
        .hasFieldOrPropertyWithValue(Comment.Fields.newsId, comment.getNewsId());
  }

  @Test
  void deleteById_whenDeleteByIdWithUnknownId_thenNotFoundExceptionExpected() {
    // given
    Integer id = CommentTestData.builder().build().buildCommentDto().id();

    doReturn(Optional.empty()).when(commentRepository).findById(id);

    // when
    NotFoundException thrown =
        assertThrows(NotFoundException.class, () -> commentService.deleteById(id));

    // then
    assertThat(thrown).hasMessage(String.format(ERROR_FORMAT_NOT_FOUND, id));
  }

  @Test
  void readAll_whenReadAllWithOffsetAndLimit_thenValidPageExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    Page<CommentDto> expected = commentTestData.buildCommentDtoPage();
    Page<Comment> commentPage = commentTestData.buildCommentPage();
    Comment comment = commentTestData.buildComment();
    CommentDto commentDto = commentTestData.buildCommentDto();

    doReturn(commentPage).when(commentRepository).findAll(expected.getPageable());
    doReturn(commentDto).when(commentMapper).toCommentDto(comment);

    // when
    Page<CommentDto> actual = commentService.readAll(PAGE_REQUEST);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void search_whenSearchWithExistTextAndKnownFieldsAndOffsetAndLimit_thenValidPageExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    Page<CommentDto> expected = commentTestData.buildCommentDtoPage();
    Page<Comment> commentPage = commentTestData.buildCommentPage();
    Comment comment = commentTestData.buildComment();
    CommentDto commentDto = commentTestData.buildCommentDto();

    doReturn(commentPage)
        .when(commentRepository)
        .searchBy(SEARCHING_TEXT, PAGE_REQUEST, SEARCHABLE_FILED);
    doReturn(commentDto).when(commentMapper).toCommentDto(comment);

    // when
    Page<CommentDto> actual =
        commentService.search(SEARCHING_TEXT, SEARCHABLE_FILED_LIST, PAGE_REQUEST);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void search_whenSearchByInvalidField_thenNotFoundExceptionExpected() {
    // given

    // when
    assertThrows(
        IllegalArgumentException.class,
        () -> commentService.search(SEARCHING_TEXT, UNKNOWN_SEARCHABLE_FILED_LIST, PAGE_REQUEST));

    // then
    // assertion in "when" block
  }

  @Test
  void readAllWithFilter_whenReadAllWithFilterAndPageRequest_thenValidPageExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    PageRequest pageRequest = commentTestData.buildPageRequest();
    Page<CommentDto> expected = commentTestData.buildCommentDtoPage();
    Page<Comment> commentPage = commentTestData.buildCommentPage();
    Comment comment = commentTestData.buildComment();
    CommentDto commentDto = commentTestData.buildCommentDto();
    Specification<Comment> specification = commentTestData.buildCommentSpecificationForId();

    doReturn(specification).when(commentSpecification).matchesFilter(commentsFilter);
    doReturn(commentPage).when(commentRepository).findAll(specification, pageRequest);
    doReturn(commentDto).when(commentMapper).toCommentDto(comment);

    // when
    Page<CommentDto> actual = commentService.readAllWithFilter(commentsFilter, pageRequest);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void deleteAllWithFilter_whenDeleteAllWithFilter_thenCallFindAllWithExpectedSpecification() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    PageRequest pageRequest = commentTestData.buildPageRequest();
    Page<Comment> commentPage = commentTestData.buildCommentPage();
    Specification<Comment> specification = commentTestData.buildCommentSpecificationForId();

    doReturn(specification).when(commentSpecification).matchesFilter(commentsFilter);
    doReturn(commentPage).when(commentRepository).findAll(specification, pageRequest);

    // when
    commentService.deleteAllWithFilter(commentsFilter);

    // then
    verify(commentRepository).findAll(specificationArgumentCaptor.capture());
    assertThat(specificationArgumentCaptor.getValue()).isEqualTo(specification);
  }

  @Test
  void deleteAllWithFilter_whenDeleteAllWithFilter_thenCallDeleteAllWithExpectedCommentList() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    List<Comment> comments = commentTestData.buildComments();
    Specification<Comment> specification = commentTestData.buildCommentSpecificationForId();

    doReturn(specification).when(commentSpecification).matchesFilter(commentsFilter);
    doReturn(comments).when(commentRepository).findAll(specification);

    // when
    commentService.deleteAllWithFilter(commentsFilter);

    // then
    verify(commentRepository).deleteAll(listArgumentCaptor.capture());
    assertThat(listArgumentCaptor.getValue()).isEqualTo(comments);
  }
}
