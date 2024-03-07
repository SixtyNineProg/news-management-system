package by.klimov.commentservice.controller;

import static by.klimov.commentservice.data.CommentTestData.PAGE_NUMBER;
import static by.klimov.commentservice.data.CommentTestData.PAGE_REQUEST;
import static by.klimov.commentservice.data.CommentTestData.PAGE_SIZE;
import static by.klimov.commentservice.data.CommentTestData.PARAM_NAME_PAGE_NUMBER;
import static by.klimov.commentservice.data.CommentTestData.PARAM_NAME_PAGE_SIZE;
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

import by.klimov.commentservice.container.PostgresSqlContainerInitializer;
import by.klimov.commentservice.data.CommentTestData;
import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.model.CommentsFilter;
import by.klimov.commentservice.service.impl.CommentServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
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
@WebMvcTest(CommentController.class)
class CommentControllerTest extends PostgresSqlContainerInitializer {

  public static final String BASE_REQUEST_MAPPING = "/comments";

  @Autowired private final MockMvc mockMvc;

  @MockBean private final CommentServiceImpl commentService;

  private final ObjectMapper objectMapper = initObjectMapper();

  @SneakyThrows
  @Test
  void getById_whenGetById_thenValidAnswerCodeAndPathAndAnswerExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentDto expected = commentTestData.buildCommentDto();
    Optional<CommentDto> optionalCommentDto = Optional.of(commentTestData.buildCommentDto());
    Integer id = expected.id();

    doReturn(optionalCommentDto).when(commentService).readById(id);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(get(BASE_REQUEST_MAPPING + "/" + id))
            .andExpectAll(status().is2xxSuccessful(), jsonPath("$.id").value(id.toString()))
            .andReturn();
    String jsonAnswerCreate = mvcResult.getResponse().getContentAsString();
    CommentDto actual = objectMapper.readValue(jsonAnswerCreate, CommentDto.class);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void getAll_whenGetAll_thenValidAnswerCodeAndContentTypeExpected() {
    // given
    doReturn(CommentTestData.builder().build().buildCommentDtoPage())
        .when(commentService)
        .readAll(PAGE_REQUEST);

    MockHttpServletRequestBuilder requestBuilder =
        get(BASE_REQUEST_MAPPING)
            .param(PARAM_NAME_PAGE_NUMBER, String.valueOf(PAGE_NUMBER))
            .param(PARAM_NAME_PAGE_SIZE, String.valueOf(PAGE_SIZE));
    requestBuilder.requestAttr(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_SIZE, PAGE_SIZE);

    // when
    mockMvc
        .perform(requestBuilder)
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
        .andExpect(request().attribute(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER))
        .andExpect(request().attribute(PARAM_NAME_PAGE_SIZE, PAGE_SIZE));

    // then
    // asserts in bock "when"
  }

  @SneakyThrows
  @Test
  void save_whenSaveCommentDto_thenValidAnswerCodeAndPathExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentDto commentDto = commentTestData.buildCommentDto();
    String jsonCommentDto = commentTestData.buildJsonCommentDto();
    doReturn(commentDto).when(commentService).create(commentDto);

    // when
    mockMvc
        .perform(
            post(BASE_REQUEST_MAPPING)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .content(jsonCommentDto))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    // then
    // asserts in bock when
  }

  @SneakyThrows
  @Test
  void deleteById_whenDeleteById_thenValidAnswerCodeAndPathExpected() {
    // given
    Integer id = CommentTestData.builder().build().buildCommentDto().id();
    doNothing().when(commentService).deleteById(id);

    // when
    mockMvc.perform(delete(BASE_REQUEST_MAPPING + "/" + id)).andExpect(status().is2xxSuccessful());

    // then
    verify(commentService, times(1)).deleteById(id);
  }

  @SneakyThrows
  @Test
  void update_whenUpdate_thenValidAnswerCode() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentDto commentDto = commentTestData.buildCommentDto();
    String jsonCommentDto = commentTestData.buildJsonCommentDto();
    doReturn(commentDto).when(commentService).update(commentDto);

    // when
    mockMvc
        .perform(
            put(BASE_REQUEST_MAPPING)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .content(jsonCommentDto))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    // then
    // asserts in bock "when"
  }

  @SneakyThrows
  @Test
  void getAllByWithFilter_whenGetAllByWithFilter_thenValidAnswerCodeAndContentTypeExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    PageRequest pageRequest = commentTestData.buildPageRequest();
    String jsonCommentFilterForId = commentTestData.buildJsonCommentFilterForId();
    Page<CommentDto> expected = commentTestData.buildCommentDtoPage();
    doReturn(expected).when(commentService).readAllWithFilter(commentsFilter, pageRequest);

    MockHttpServletRequestBuilder requestBuilder =
        post(BASE_REQUEST_MAPPING + "/filter")
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .param(PARAM_NAME_PAGE_NUMBER, String.valueOf(PAGE_NUMBER))
            .param(PARAM_NAME_PAGE_SIZE, String.valueOf(PAGE_SIZE))
            .content(jsonCommentFilterForId);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_SIZE, PAGE_SIZE);

    // when
    MvcResult mvcResult =
        mockMvc
            .perform(requestBuilder)
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType(APPLICATION_JSON_VALUE))
            .andExpect(request().attribute(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER))
            .andExpect(request().attribute(PARAM_NAME_PAGE_SIZE, PAGE_SIZE))
            .andReturn();
    String jsonAnswer = mvcResult.getResponse().getContentAsString();
    Page<CommentDto> actual = getCommentDtosPage(jsonAnswer);

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @SneakyThrows
  @Test
  void deleteAllByWithFilter_whenDeleteAllByWithFilter_thenValidAnswerCodeExpected() {
    // given
    CommentTestData commentTestData = CommentTestData.builder().build();
    CommentsFilter commentsFilter = commentTestData.buildCommentFilterForId();
    String jsonCommentFilterForId = commentTestData.buildJsonCommentFilterForId();
    PageRequest pageRequest = commentTestData.buildPageRequest();
    Page<CommentDto> commentDtos = commentTestData.buildCommentDtoPage();

    doReturn(commentDtos).when(commentService).readAllWithFilter(commentsFilter, pageRequest);
    doNothing().when(commentService).deleteAllWithFilter(commentsFilter);

    MockHttpServletRequestBuilder requestBuilder =
        post(BASE_REQUEST_MAPPING + "/delete")
            .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .content(jsonCommentFilterForId);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_NUMBER, PAGE_NUMBER);
    requestBuilder.requestAttr(PARAM_NAME_PAGE_SIZE, PAGE_SIZE);

    // when
    mockMvc.perform(requestBuilder).andExpect(status().is2xxSuccessful());

    // then
    // asserts in bock "when"
  }

  private ObjectMapper initObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }

  private Page<CommentDto> getCommentDtosPage(String jsonString) throws JsonProcessingException {
    JsonNode rootNode = objectMapper.readTree(jsonString);
    JsonNode contentNode = rootNode.get("content");
    List<CommentDto> comments = objectMapper.convertValue(contentNode, new TypeReference<>() {});

    int pageNumber = rootNode.get("pageable").get("pageNumber").asInt();
    int pageSize = rootNode.get("pageable").get("pageSize").asInt();
    PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

    return new PageImpl<>(comments, pageRequest, rootNode.get("totalElements").asLong());
  }
}
