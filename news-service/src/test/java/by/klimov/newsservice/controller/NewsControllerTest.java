package by.klimov.newsservice.controller;

import static by.klimov.newsservice.data.NewsTestData.COMMENTS_PAGE_SIZE;
import static by.klimov.newsservice.data.NewsTestData.PAGE_NUMBER;
import static by.klimov.newsservice.data.NewsTestData.PAGE_REQUEST;
import static by.klimov.newsservice.data.NewsTestData.PAGE_SIZE;
import static by.klimov.newsservice.data.NewsTestData.PARAM_NAME_COMMENTS_PAGE_SIZE;
import static by.klimov.newsservice.data.NewsTestData.PARAM_NAME_FIELDS;
import static by.klimov.newsservice.data.NewsTestData.PARAM_NAME_PAGE_NUMBER;
import static by.klimov.newsservice.data.NewsTestData.PARAM_NAME_PAGE_SIZE;
import static by.klimov.newsservice.data.NewsTestData.PARAM_NAME_TEXT;
import static by.klimov.newsservice.data.NewsTestData.SEARCHABLE_FILED;
import static by.klimov.newsservice.data.NewsTestData.SEARCHABLE_FILED_LIST;
import static by.klimov.newsservice.data.NewsTestData.SEARCH_TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.klimov.newsservice.container.PostgresSqlContainerInitializer;
import by.klimov.newsservice.data.CommentTestData;
import by.klimov.newsservice.data.NewsTestData;
import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.dto.NewsDto;
import by.klimov.newsservice.service.impl.NewsServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@AutoConfigureDataJpa
@RequiredArgsConstructor
@WebMvcTest(NewsController.class)
public class NewsControllerTest extends PostgresSqlContainerInitializer {

  public static final String BASE_REQUEST_MAPPING = "/news";

  @Autowired private MockMvc mockMvc;

  @MockBean private NewsServiceImpl newsService;

  private final ObjectMapper objectMapper = initObjectMapper();

  @SneakyThrows
  @Test
  void save_whenSaveDto_thenValidAnswerCodeAndPathExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    NewsDto newsDto = newsTestData.buildNewsDto();
    String jsonNewsDto = newsTestData.buildJsonNewsDto();
    doReturn(newsDto).when(newsService).create(newsDto);

    // when
    mockMvc
        .perform(
            post(BASE_REQUEST_MAPPING)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .content(jsonNewsDto))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    // then
    // asserts in bock when
  }

  @SneakyThrows
  @Test
  void getAll_whenGetAll_thenValidAnswerCodeAndContentTypeExpected() {
    // given
    doReturn(NewsTestData.builder().build().buildNewsDtoWithCommentsPage())
        .when(newsService)
        .readAll(PAGE_REQUEST, COMMENTS_PAGE_SIZE);

    MockHttpServletRequestBuilder requestBuilder =
        get(BASE_REQUEST_MAPPING)
            .param(PARAM_NAME_PAGE_NUMBER, String.valueOf(PAGE_NUMBER))
            .param(PARAM_NAME_PAGE_SIZE, String.valueOf(PAGE_SIZE))
            .param(PARAM_NAME_COMMENTS_PAGE_SIZE, String.valueOf(COMMENTS_PAGE_SIZE));
    requestBuilder.requestAttr(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_SIZE, PAGE_SIZE);
    requestBuilder.requestAttr(PARAM_NAME_COMMENTS_PAGE_SIZE, COMMENTS_PAGE_SIZE);

    // when
    mockMvc
        .perform(requestBuilder)
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(request().attribute(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER))
        .andExpect(request().attribute(PARAM_NAME_PAGE_SIZE, PAGE_SIZE))
        .andExpect(request().attribute(PARAM_NAME_COMMENTS_PAGE_SIZE, COMMENTS_PAGE_SIZE));

    // then
    // asserts in bock "when"
  }

  @SneakyThrows
  @Test
  void search() {
    // given
    doReturn(NewsTestData.builder().build().buildNewsDtoWithCommentsPage())
        .when(newsService)
        .search(SEARCH_TEXT, SEARCHABLE_FILED_LIST, PAGE_REQUEST, COMMENTS_PAGE_SIZE);

    MockHttpServletRequestBuilder requestBuilder =
        get(BASE_REQUEST_MAPPING + "/search")
            .param(PARAM_NAME_TEXT, SEARCH_TEXT)
            .param(PARAM_NAME_FIELDS, SEARCHABLE_FILED)
            .param(PARAM_NAME_PAGE_NUMBER, String.valueOf(PAGE_NUMBER))
            .param(PARAM_NAME_PAGE_SIZE, String.valueOf(PAGE_SIZE))
            .param(PARAM_NAME_COMMENTS_PAGE_SIZE, String.valueOf(COMMENTS_PAGE_SIZE));
    requestBuilder.requestAttr(PARAM_NAME_TEXT, SEARCH_TEXT);
    requestBuilder.requestAttr(PARAM_NAME_FIELDS, SEARCHABLE_FILED);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_SIZE, PAGE_SIZE);
    requestBuilder.requestAttr(PARAM_NAME_COMMENTS_PAGE_SIZE, COMMENTS_PAGE_SIZE);

    // when
    mockMvc
        .perform(requestBuilder)
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(request().attribute(PARAM_NAME_TEXT, SEARCH_TEXT))
        .andExpect(request().attribute(PARAM_NAME_FIELDS, SEARCHABLE_FILED))
        .andExpect(request().attribute(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER))
        .andExpect(request().attribute(PARAM_NAME_PAGE_SIZE, PAGE_SIZE))
        .andExpect(request().attribute(PARAM_NAME_COMMENTS_PAGE_SIZE, COMMENTS_PAGE_SIZE));

    // then
    // asserts in bock "when"
  }

  @SneakyThrows
  @Test
  void getById_whenGetById_thenValidAnswerCodeAndPathAndAnswerExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    NewsDto expected = newsTestData.buildNewsDto();
    Integer id = expected.getId();

    doReturn(expected).when(newsService).readById(id, COMMENTS_PAGE_SIZE);

    MockHttpServletRequestBuilder requestBuilder =
        get(BASE_REQUEST_MAPPING + "/" + id)
            .param(PARAM_NAME_COMMENTS_PAGE_SIZE, String.valueOf(COMMENTS_PAGE_SIZE));
    requestBuilder.requestAttr(PARAM_NAME_COMMENTS_PAGE_SIZE, COMMENTS_PAGE_SIZE);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(request().attribute(PARAM_NAME_COMMENTS_PAGE_SIZE, COMMENTS_PAGE_SIZE))
            .andReturn();
    String jsonAnswerCreate = mvcResult.getResponse().getContentAsString();
    NewsDto actual = objectMapper.readValue(jsonAnswerCreate, NewsDto.class);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void getCommentsByNewsId_whenGetCommentsByNewsId_thenValidAnswerCodeAndPathAndAnswerExpected() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    Page<NewsDto> expected = newsTestData.buildNewsDtoPage();
    Integer id = newsTestData.buildNewsDto().getId();

    doReturn(expected).when(newsService).readCommentsByNewsId(id, PAGE_REQUEST);

    MockHttpServletRequestBuilder requestBuilder =
        get(BASE_REQUEST_MAPPING + "/{id}/comments", id)
            .param(PARAM_NAME_PAGE_NUMBER, String.valueOf(PAGE_NUMBER))
            .param(PARAM_NAME_PAGE_SIZE, String.valueOf(PAGE_SIZE));
    requestBuilder.requestAttr(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_SIZE, PAGE_SIZE);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(jsonPath("$.content[0].id").value(id.toString()))
            .andExpect(request().attribute(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER))
            .andExpect(request().attribute(PARAM_NAME_PAGE_SIZE, PAGE_SIZE))
            .andReturn();
    String jsonAnswer = mvcResult.getResponse().getContentAsString();
    Page<NewsDto> actual = getCommentDtosPage(jsonAnswer);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void
      getCommentsByIdFromNews_whenGetCommentsByIdFromNews_thenValidAnswerCodeAndPathAndAnswerExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    NewsTestData newsTestData = NewsTestData.builder().build();
    Integer newsId = newsTestData.buildNewsDtoWithComments().getId();
    Integer commentId = commentTestData.buildCommentDto().id();
    CommentDto expected = commentTestData.buildCommentDto();

    doReturn(expected).when(newsService).readCommentByIdFromNews(newsId, commentId);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(get(BASE_REQUEST_MAPPING + "/{id}/comments/{commentId}", newsId, commentId))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
    String jsonAnswer = mvcResult.getResponse().getContentAsString();
    CommentDto actual = objectMapper.readValue(jsonAnswer, CommentDto.class);

    // then
    verify(newsService).readCommentByIdFromNews(newsId, commentId);
    assertThat(actual).isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void update_whenUpdate_thenValidAnswerCode() {
    // given
    NewsTestData newsTestData = NewsTestData.builder().build();
    NewsDto dto = newsTestData.buildNewsDto();
    String jsonDto = newsTestData.buildJsonNewsDto();
    doReturn(dto).when(newsService).update(dto);

    // when
    mockMvc
        .perform(
            put(BASE_REQUEST_MAPPING).header(CONTENT_TYPE, APPLICATION_JSON_VALUE).content(jsonDto))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    // then
    // asserts in bock "when"
  }

  @SneakyThrows
  @Test
  void deleteById_whenDeleteById_thenValidAnswerCodeAndPathExpected() {
    // given
    Integer id = NewsTestData.builder().build().buildNewsDtoWithComments().getId();
    doNothing().when(newsService).deleteById(id);

    // when
    mockMvc.perform(delete(BASE_REQUEST_MAPPING + "/" + id)).andExpect(status().is2xxSuccessful());

    // then
    verify(newsService, times(1)).deleteById(id);
  }

  private ObjectMapper initObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }

  private Page<NewsDto> getCommentDtosPage(String jsonString) throws JsonProcessingException {
    JsonNode rootNode = objectMapper.readTree(jsonString);
    JsonNode contentNode = rootNode.get("content");
    List<NewsDto> newsDtos = objectMapper.convertValue(contentNode, new TypeReference<>() {});

    int pageNumber = rootNode.get("pageable").get("pageNumber").asInt();
    int pageSize = rootNode.get("pageable").get("pageSize").asInt();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

    return new PageImpl<>(newsDtos, pageRequest, rootNode.get("totalElements").asLong());
  }
}
