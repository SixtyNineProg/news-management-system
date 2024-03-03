package by.klimov.newsservice.model;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Comment {

  private Integer id;
  private Timestamp time;
  private String text;
  private String userName;
  private Integer newsId;
}
