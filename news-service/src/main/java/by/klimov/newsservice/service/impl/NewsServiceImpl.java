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
  public static final String ERROR_FORMAT_COMMENT_NEWS_NOT_FOUND =
      "Comment with id = %s in news with id = %s not found";
  public static final int DEFAULT_COMMENT_DTO_PAGE_SIZE = 10;

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
   * This method reads a news item by its ID and returns it along with its comments.
   *
   * @param id The ID of the news item to be read.
   * @param commentsPageSize The size of the page for comments to be returned.
   * @return A NewsDto object that contains the news item and its comments.
   * @throws NotFoundException If the news item with the given ID is not found.
   */
  @Override
  public NewsDto readById(Integer id, Integer commentsPageSize) {
    Optional<News> optionalNews = newsRepository.findById(id);
    News news =
        optionalNews.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, id)));
    NewsDto newsDto = newsMapper.toDto(news);
    List<Page<CommentDto>> commentDtos =
        commentService.getAllCommentDtoPagesWithFilter(new CommentsFilter(id), commentsPageSize);
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
   * Deletes a news item by its ID and also deletes its associated comments.
   *
   * @param id The ID of the news item to be deleted.
   * @throws NotFoundException If the news item with the given ID is not found.
   */
  @Override
  public void deleteById(Integer id) {
    Optional<News> optionalNews = newsRepository.findById(id);
    News news =
        optionalNews.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, id)));
    newsRepository.delete(news);
    commentService.deleteAllWithFilter(new CommentsFilter(id));
  }

  /**
   * Retrieves all news items and their comments in a paginated format.
   *
   * @param pageRequest The pagination and sorting details for the news items.
   * @param commentsPageSize The size of the page for comments.
   * @return A paginated list of news items and their comments.
   */
  @Override
  public Page<NewsDto> readAll(PageRequest pageRequest, Integer commentsPageSize) {
    Page<News> newsPage = newsRepository.findAll(pageRequest);
    Page<NewsDto> newsDtoPage = newsPage.map(newsMapper::toDto);
    List<NewsDto> newsDtos = newsDtoPage.getContent();
    newsDtos.forEach(
        newsDto ->
            newsDto.setComments(
                commentService.getAllCommentDtoPagesWithFilter(
                    new CommentsFilter(newsDto.getId()), commentsPageSize)));
    return newsDtoPage;
  }

  /**
   * Retrieves all comments for a specific news item in a paginated format.
   *
   * @param newsId The ID of the news item for which comments are to be retrieved.
   * @param pageRequest The pagination and sorting details for the comments.
   * @return A paginated list of comments for the specified news item.
   */
  @Override
  public Page<CommentDto> readCommentsByNewsId(Integer newsId, PageRequest pageRequest) {
    return commentService.getCommentDtoPageWithFilter(pageRequest, new CommentsFilter(newsId));
  }

  /**
   * Retrieves a specific comment from a specific news item.
   *
   * @param newsId The ID of the news item from which the comment is to be retrieved.
   * @param commentId The ID of the comment to be retrieved.
   * @return The CommentDto object that represents the comment.
   * @throws NotFoundException If the comment with the given ID is not found in the news item with the given ID.
   */
  @Override
  public CommentDto readCommentByIdFromNews(Integer newsId, Integer commentId) {
    List<Page<CommentDto>> commentDtos =
        commentService.getAllCommentDtoPagesWithFilter(
            new CommentsFilter(newsId), DEFAULT_COMMENT_DTO_PAGE_SIZE);
    return commentDtos.stream()
        .flatMap(page -> page.getContent().stream())
        .filter(commentDto -> commentDto.id().equals(commentId))
        .findFirst()
        .orElseThrow(
            () ->
                new NotFoundException(
                    String.format(ERROR_FORMAT_COMMENT_NEWS_NOT_FOUND, commentId, newsId)));
  }

  /**
   * Searches for news items based on the provided text and fields, and returns them along with
   * their comments in a paginated format.
   *
   * @param text The text to search for in the news items.
   * @param fields The fields in the news items to search by. If empty, all searchable fields are
   *     used.
   * @param pageRequest The pagination and sorting details for the news items.
   * @param commentsPageSize The size of the page for comments.
   * @return A paginated list of news items and their comments that match the search criteria.
   * @throws IllegalArgumentException If any of the provided fields to search by are not searchable.
   */
  @Override
  public Page<NewsDto> search(
      String text, List<String> fields, PageRequest pageRequest, Integer commentsPageSize) {
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
    Page<NewsDto> newsDtoPage = newsPage.map(newsMapper::toDto);
    List<NewsDto> newsDtos = newsDtoPage.getContent();
    newsDtos.forEach(
        newsDto ->
            newsDto.setComments(
                commentService.getAllCommentDtoPagesWithFilter(
                    new CommentsFilter(newsDto.getId()), commentsPageSize)));
    return newsDtoPage;
  }
}
