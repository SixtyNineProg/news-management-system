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
import by.klimov.commentservice.service.impl.CommentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
  void get_whenGet_thenValidAnswerCodeAndContentTypeExpected() {
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
    CommentDto CommentDto = commentTestData.buildCommentDto();
    String jsonCommentDto = commentTestData.buildJsonCommentDto();
    doReturn(CommentDto).when(commentService).create(CommentDto);

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
  void deleteByUuid_whenDeleteByUuid_thenValidAnswerCodeAndPathExpected() {
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
    CommentDto CommentDto = commentTestData.buildCommentDto();
    String jsonCommentDto = commentTestData.buildJsonCommentDto();
    doReturn(CommentDto).when(commentService).update(CommentDto);

    // when
    mockMvc
        .perform(
            put(BASE_REQUEST_MAPPING)
                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .content(jsonCommentDto))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON_VALUE));

    // then
    // asserts in bock when
  }

  private ObjectMapper initObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }
}
