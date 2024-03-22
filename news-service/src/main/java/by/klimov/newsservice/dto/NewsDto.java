package by.klimov.newsservice.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.domain.Page;

@Data
@Builder
@FieldNameConstants
public class NewsDto {

  private final Integer id;
  private final LocalDateTime time;
  private final @NotBlank String title;
  private final @NotBlank String text;
  private List<Page<CommentDto>> comments;
}
