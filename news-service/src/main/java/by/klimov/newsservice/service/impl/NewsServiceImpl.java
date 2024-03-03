package by.klimov.newsservice.service.impl;

import by.klimov.newsservice.dto.CommentDto;
import by.klimov.newsservice.dto.NewsDto;
import by.klimov.newsservice.entity.News;
import by.klimov.newsservice.exception.NotFoundException;
import by.klimov.newsservice.mapper.NewsMapper;
import by.klimov.newsservice.model.CommentsFilter;
import by.klimov.newsservice.repository.NewsRepository;
import by.klimov.newsservice.service.CommentService;
import by.klimov.newsservice.service.NewsService;
import by.klimov.newsservice.util.ReflectionUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

  public static final String ERROR_FORMAT_NOT_FOUND = "News with id = %s not found";

  private final NewsRepository newsRepository;
  private final NewsMapper newsMapper;
  private final CommentService commentService;

  /**
   * This method is used to create a new News object from a given NewsDto. It first converts the
   * NewsDto to a News object, then saves it in the repository. Finally, it converts the saved News
   * object back to a NewsDto before returning.
   *
   * @param newsDto The NewsDto object that contains the data for the new News object.
   * @return The NewsDto object that was saved in the repository.
   * @override
   */
  @Override
  public NewsDto create(NewsDto newsDto) {
    News news = newsMapper.toEntity(newsDto);
    News repositoryNews = newsRepository.save(news);
    return newsMapper.toDto(repositoryNews);
  }

  /**
   * This method is used to read a News object by its ID from the repository. It first finds the
   * News object in the repository by its ID. If the News object exists, it converts the News object
   * to a NewsDto before returning.
   *
   * @param id The ID of the News object to be read from the repository.
   * @return An Optional that may contain the NewsDto object if one with the given ID exists in the
   *     repository.
   * @override
   */
  @Override
  public NewsDto readById(Integer id, Integer commentsPageSize) {
    Optional<News> optionalNews = newsRepository.findById(id);
    News news =
        optionalNews.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, id)));
    NewsDto newsDto = newsMapper.toDto(news);
    CommentsFilter commentsFilter = new CommentsFilter(id);
    List<Page<CommentDto>> commentDtos =
        commentService.getAllCommentDtoPagesWithFilter(commentsFilter, commentsPageSize);
    newsDto.setComments(commentDtos);
    return newsDto;
  }

  /**
   * This method is used to update a News object in the repository with the data from a given
   * NewsDto. It first finds the News object in the repository by its ID. If the News object does
   * not exist, it throws a NotFoundException. Otherwise, it merges the data from the NewsDto into
   * the News object, saves the updated News object in the repository, and then converts the saved
   * News object back to a NewsDto before returning.
   *
   * @param newsDto The NewsDto object that contains the updated data for the News object.
   * @return The updated NewsDto object that was saved in the repository.
   * @throws NotFoundException if no News object with the given ID exists in the repository.
   * @override
   */
  @Override
  public NewsDto update(NewsDto newsDto) {
    Optional<News> optionalNews = newsRepository.findById(newsDto.getId());
    News news =
        optionalNews.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, newsDto.getId())));
    news = newsMapper.merge(newsDto, news);
    return newsMapper.toDto(newsRepository.save(news));
  }

  /**
   * This method is used to delete a News object by its ID from the repository. It first finds the
   * News object in the repository by its ID. If the News object does not exist, it throws a
   * NotFoundException. Otherwise, it deletes the News object from the repository.
   *
   * @param id The ID of the News object to be deleted from the repository.
   * @throws NotFoundException if no News object with the given ID exists in the repository.
   * @override
   */
  @Override
  public void deleteById(Integer id) {
    Optional<News> optionalNews = newsRepository.findById(id);
    News news =
        optionalNews.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, id)));
    newsRepository.delete(news);
  }

  /**
   * This method is used to read all News objects from the repository and return them as a Page of
   * NewsDto objects. It first finds all News objects in the repository and returns them as a Page.
   * Then, it converts each News object in the Page to a NewsDto.
   *
   * @param pageRequest The PageRequest object that specifies the pagination information.
   * @return A Page of NewsDto objects that represent all News objects in the repository.
   * @override
   */
  @Override
  public Page<NewsDto> readAll(PageRequest pageRequest) {
    Page<News> newsPage = newsRepository.findAll(pageRequest);
    return newsPage.map(newsMapper::toDto);
  }

  /**
   * This method is used to search for News objects in the repository based on a given text and
   * fields, and return them as a Page of NewsDto objects. It first gets the list of searchable
   * fields from the News class. If no fields are provided, it uses all searchable fields for the
   * search. If any of the provided fields are not searchable, it throws an
   * IllegalArgumentException. Then, it searches the repository with the given text, pagination
   * information, and fields, and returns the results as a Page of NewsDto objects.
   *
   * @param text The text to search for in the specified fields.
   * @param fields The fields to search in. If empty, all searchable fields are used.
   * @param pageRequest The PageRequest object that specifies the pagination information.
   * @return A Page of NewsDto objects that match the search criteria.
   * @throws IllegalArgumentException if any of the provided fields are not searchable.
   * @override
   */
  @Override
  public Page<NewsDto> search(String text, List<String> fields, PageRequest pageRequest) {
    List<String> searchableFields =
        ReflectionUtil.getAnnotatedFieldNames(News.class, FullTextField.class);

    List<String> fieldsToSearchBy = fields.isEmpty() ? searchableFields : fields;

    boolean containsInvalidField =
        fieldsToSearchBy.stream().anyMatch(field -> !searchableFields.contains(field));

    if (containsInvalidField) {
      throw new IllegalArgumentException();
    }

    Page<News> newsPage =
        newsRepository.searchBy(text, pageRequest, fieldsToSearchBy.toArray(new String[0]));
    return newsPage.map(newsMapper::toDto);
  }
}
