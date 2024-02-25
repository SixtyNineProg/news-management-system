package by.klimov.commentservice.mapper;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.entity.Comment;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

  CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

  @Mapping(target = "time", source = "time", qualifiedByName = "toLocalDateTime")
  CommentDto toCommentDto(Comment comment);

  @Mapping(target = "time", source = "time", qualifiedByName = "toLocalDateTime")
  List<CommentDto> toCommentDto(List<CommentDto> commentDtoList);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "time", source = "time", qualifiedByName = "toTimestamp")
  Comment toComment(CommentDto commentDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "time", source = "time", qualifiedByName = "toTimestamp")
  Comment merge(CommentDto source, @MappingTarget Comment target);

  @Named("toLocalDateTime")
  default LocalDateTime map(Timestamp timestamp) {
    return timestamp == null
        ? null
        : timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
  }

  @Named("toTimestamp")
  default Timestamp map(LocalDateTime localDateTime) {
    return localDateTime != null ? Timestamp.valueOf(localDateTime) : null;
  }
}
