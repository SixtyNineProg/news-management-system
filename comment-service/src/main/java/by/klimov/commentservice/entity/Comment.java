package by.klimov.commentservice.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

@Data
@Entity
@Builder
@Indexed
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Table(name = "comment")
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @CreationTimestamp(source = SourceType.DB)
  private Timestamp time;

  @NotBlank
  @FullTextField
  private String text;

  @NotBlank
  @FullTextField
  @Column(name = "username")
  private String userName;

  @Column(name = "news_id")
  private Integer newsId;
}
