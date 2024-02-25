package by.klimov.commentservice.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record CommentDto(Integer id, LocalDateTime time, String text, String userName, Integer newsId) {}
