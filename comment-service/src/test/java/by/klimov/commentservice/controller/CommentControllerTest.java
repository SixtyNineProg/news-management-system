package by.klimov.commentservice.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.klimov.commentservice.data.CommentTestData;
import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.lucene.Indexer;
import by.klimov.commentservice.service.impl.CommentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest
@RequiredArgsConstructor
class CommentControllerTest {

  public static final String BASE_REQUEST_MAPPING = "/comments";

  private final MockMvc mockMvc;

  @MockBean private final CommentServiceImpl commentService;

  @MockBean private final Indexer indexer;

  private final ObjectMapper objectMapper = initObjectMapper();

//  @SneakyThrows
  @Test
  void getById_whenGetById_thenValidAnswerCodeAndPathAndAnswerExpected() {
//    // given
//    CommentTestData commentTestData = CommentTestData.builder().build();
//    CommentDto expected = commentTestData.buildCommentDto();
//    Optional<CommentDto> optionalCommentDto = Optional.of(commentTestData.buildCommentDto());
//    Integer id = expected.id();
//
//    doReturn(optionalCommentDto).when(commentService).readById(id);
//
//    // when
//    MvcResult mvcResult =
//        mockMvc
//            .perform(get(BASE_REQUEST_MAPPING + "/" + id))
//            .andExpectAll(status().is2xxSuccessful(), jsonPath("$.id").value(id.toString()))
//            .andReturn();
//    String jsonAnswerCreate = mvcResult.getResponse().getContentAsString();
//    CommentDto actual = objectMapper.readValue(jsonAnswerCreate, CommentDto.class);
//
//    // then
//    assertThat(actual).isEqualTo(expected);
  }

  //
  //  @SneakyThrows
  //  @Test
  //  void get_whenGet_thenValidAnswerCodeAndContentTypeExpected() {
  //    // given
  //    doReturn(CommentTestData.builder().build().buildCommentDtoPage())
  //        .when(CommentService)
  //        .readAll(CommentTestData.OFFSET, CommentTestData.LIMIT);
  //
  //    MockHttpServletRequestBuilder requestBuilder =
  //        get(BASE_REQUEST_MAPPING)
  //            .param(OFFSET, String.valueOf(CommentTestData.OFFSET))
  //            .param(LIMIT, String.valueOf(CommentTestData.LIMIT));
  //    requestBuilder.requestAttr(OFFSET, CommentTestData.OFFSET);
  //    requestBuilder.requestAttr(LIMIT, CommentTestData.LIMIT);
  //
  //    // when
  //    mockMvc
  //        .perform(requestBuilder)
  //        .andExpect(status().is2xxSuccessful())
  //        .andExpect(content().contentType(APPLICATION_JSON_VALUE))
  //        .andExpect(request().attribute(OFFSET, CommentTestData.OFFSET))
  //        .andExpect(request().attribute(LIMIT, CommentTestData.LIMIT));
  //
  //    // then
  //    // asserts in bock when
  //  }
  //
  //  @SneakyThrows
  //  @Test
  //  void getOwnedHousesByCommentUuid_whenGetByUuid_thenValidAnswerCodeAndPathExpected() {
  //    // given
  //    UUID uuid = HouseTestData.builder().build().buildHouse().getUuid();
  //    doReturn(HouseTestData.builder().build().buildHouseDtos())
  //        .when(CommentService)
  //        .getOwnedHousesByCommentUuid(uuid);
  //
  //    // when
  //    mockMvc
  //        .perform(get(BASE_REQUEST_MAPPING + "/" + uuid + "/houses"))
  //        .andExpect(status().is2xxSuccessful())
  //        .andExpect(jsonPath("$[0].uuid", is(uuid.toString())));
  //
  //    // then
  //    // asserts in bock when
  //  }
  //
  //  @SneakyThrows
  //  @Test
  //  void save_whenSaveCommentDto_thenValidAnswerCodeAndPathExpected() {
  //    // given
  //    CommentTestData CommentTestData = CommentTestData.builder().build();
  //    CommentDto CommentDto = CommentTestData.builder().build().buildCommentDto();
  //    String jsonCommentDto = CommentTestData.jsonCommentDto();
  //    doReturn(CommentDto).when(CommentService).create(CommentDto);
  //
  //    // when
  //    mockMvc
  //        .perform(
  //            post(BASE_REQUEST_MAPPING)
  //                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
  //                .content(jsonCommentDto))
  //        .andExpect(status().isCreated())
  //        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  //
  //    // then
  //    // asserts in bock when
  //  }
  //
  //  @SneakyThrows
  //  @Test
  //  void deleteByUuid_whenDeleteByUuid_thenValidAnswerCodeAndPathExpected() {
  //    // given
  //    UUID uuid = CommentTestData.builder().build().buildCommentDto().uuid();
  //    doNothing().when(CommentService).deleteByUuid(uuid);
  //
  //    // when
  //    mockMvc
  //        .perform(delete(BASE_REQUEST_MAPPING + "/" + uuid))
  //        .andExpect(status().is2xxSuccessful());
  //
  //    // then
  //    verify(CommentService, times(1)).deleteByUuid(uuid);
  //  }
  //
  //  @SneakyThrows
  //  @Test
  //  void update_whenUpdate_thenValidAnswerCode() {
  //    // given
  //    CommentTestData CommentTestData = CommentTestData.builder().build();
  //    CommentDto CommentDto = CommentTestData.builder().build().buildCommentDto();
  //    String jsonCommentDto = CommentTestData.jsonCommentDto();
  //    doReturn(CommentDto).when(CommentService).update(CommentDto);
  //
  //    // when
  //    mockMvc
  //        .perform(
  //            put(BASE_REQUEST_MAPPING)
  //                .header(CONTENT_TYPE, APPLICATION_JSON_VALUE)
  //                .content(jsonCommentDto))
  //        .andExpect(status().isCreated())
  //        .andExpect(content().contentType(APPLICATION_JSON_VALUE));
  //
  //    // then
  //    // asserts in bock when
  //  }
  //
  private ObjectMapper initObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    return objectMapper;
  }
}
