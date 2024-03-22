package by.klimov.newsservice.service.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import by.klimov.newsservice.container.PostgresSqlContainerInitializer;
import by.klimov.newsservice.data.CommentTestData;
import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.exception.CommentServiceException;
import by.klimov.newsservice.model.CommentsFilter;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@RequiredArgsConstructor
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
class FeignCommentServiceTest extends PostgresSqlContainerInitializer {

  @Autowired FeignCommentService feignCommentService;

  @SneakyThrows
  @Test
  void getAllCommentDtoPagesWithFilter_whenFilterApplied_thenListOfPagesReturned() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    List<Page<CommentDto>> expected = commentTestData.buildListCommentDtoPage();
    stubFor(
        post(urlEqualTo("/v1/comments/filter?page_number=0&page_size=4"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(commentTestData.buildJsonListCommentDtoPage())
                    .withStatus(200)));
    // when
    List<Page<CommentDto>> actual =
        feignCommentService.getAllCommentDtoPagesWithFilter(
            commentsFilter, CommentTestData.PAGE_SIZE);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void getAllCommentDtoPagesWithFilter_whenFilterApplied_thenExceptionThrown() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    stubFor(
        post(urlEqualTo("/v1/comments/filter?page_number=0&page_size=4"))
            .willReturn(aResponse().withStatus(201)));

    // when
    assertThrows(
        CommentServiceException.class,
        () ->
            feignCommentService.getAllCommentDtoPagesWithFilter(
                commentsFilter, CommentTestData.PAGE_SIZE));

    // then
    // assertion in "when" block
  }

  @Test
  void deleteAllWithFilter_whenFilterApplied_thenExceptionThrown() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    stubFor(post(urlEqualTo("/v1/comments/delete")).willReturn(aResponse().withStatus(201)));

    // when
    assertThrows(
        CommentServiceException.class,
        () -> feignCommentService.deleteAllWithFilter(commentsFilter));

    // then
    // assertion in "when" block
  }

  @SneakyThrows
  @Test
  void getCommentDtoPageWithFilter_whenFilterApplied_thenPageOfCommentsReturned() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    Page<CommentDto> expected = commentTestData.buildCommentDtoPage();
    stubFor(
        post(urlEqualTo("/v1/comments/filter?page_number=1&page_size=4"))
            .willReturn(
                aResponse()
                    .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .withBody(commentTestData.buildJsonListCommentDtoPage())
                    .withStatus(200)));
    // when
    Page<CommentDto> actual =
        feignCommentService.getCommentDtoPageWithFilter(
            CommentTestData.PAGE_REQUEST, commentsFilter);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void getCommentDtoPageWithFilter_whenFilterApplied_thenExceptionThrown() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    stubFor(
        post(urlEqualTo("/v1/comments/filter?page_number=1&page_size=4"))
            .willReturn(aResponse().withStatus(201)));

    // when
    assertThrows(
        CommentServiceException.class,
        () ->
            feignCommentService.getCommentDtoPageWithFilter(
                CommentTestData.PAGE_REQUEST, commentsFilter));

    // then
    // assertion in "when" block
  }
}
