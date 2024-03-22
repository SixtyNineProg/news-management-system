package by.klimov.newsservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@Builder
@FieldNameConstants
public record CommentDto(
    Integer id,
    LocalDateTime time,
    @NotBlank String text,
    @NotBlank String userName,
    Integer newsId) {}
