package by.klimov.newsservice.mapper;

import by.klimov.newsservice.dto.NewsDto;
import by.klimov.newsservice.entity.News;
import by.klimov.newsservice.service.NewsService;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    uses = NewsService.class,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface NewsMapper {

  NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "time", source = "time", qualifiedByName = "toLocalDateTime")
  NewsDto toDto(News news);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "time", source = "time", qualifiedByName = "toTimestamp")
  News toEntity(NewsDto newsDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "time", source = "time", qualifiedByName = "toTimestamp")
  News merge(NewsDto source, @MappingTarget News target);

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
