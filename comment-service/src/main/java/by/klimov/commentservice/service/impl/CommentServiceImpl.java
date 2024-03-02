package by.klimov.commentservice.service.impl;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.entity.Comment;
import by.klimov.commentservice.exception.NotFoundException;
import by.klimov.commentservice.mapper.CommentMapper;
import by.klimov.commentservice.model.CommentsFilter;
import by.klimov.commentservice.repository.CommentRepository;
import by.klimov.commentservice.service.CommentService;
import by.klimov.commentservice.specification.CommentSpecification;
import by.klimov.commentservice.util.ReflectionUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

  public static final String ERROR_FORMAT_NOT_FOUND = "Comment with id = %s not found";

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  public CommentServiceImpl(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
    this.commentMapper = CommentMapper.INSTANCE;
  }

  /**
   * This method is used to create a new comment.
   *
   * @param commentDto An instance of CommentDto which contains the data of the comment to be
   *     created.
   * @return CommentDto Returns the created comment as an instance of CommentDto.
   * @throws IllegalArgumentException If the provided CommentDto is null. {@code @override}
   */
  @Override
  public CommentDto create(CommentDto commentDto) {
    Comment comment = commentMapper.toComment(commentDto);
    Comment repositoryComment = commentRepository.save(comment);
    return commentMapper.toCommentDto(repositoryComment);
  }

  /**
   * This method is used to read a comment by its ID.
   *
   * @param id The ID of the comment to be read.
   * @return Optional<CommentDto> Returns an Optional that contains the CommentDto if found, or an
   *     empty Optional if not.
   * @throws IllegalArgumentException If the provided ID is null. {@code @override}
   */
  @Override
  public Optional<CommentDto> readById(Integer id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    return optionalComment.map(commentMapper::toCommentDto);
  }

  /**
   * This method is used to update a comment.
   *
   * @param commentDto An instance of CommentDto which contains the updated data of the comment.
   * @return CommentDto Returns the updated comment as an instance of CommentDto.
   * @throws NotFoundException If the comment with the provided ID is not found. {@code @override}
   */
  @Override
  public CommentDto update(CommentDto commentDto) {
    Optional<Comment> optionalComment = commentRepository.findById(commentDto.id());
    Comment comment =
        optionalComment.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, commentDto.id())));
    comment = commentMapper.merge(commentDto, comment);
    return commentMapper.toCommentDto(commentRepository.save(comment));
  }

  /**
   * This method is used to delete a comment by its ID.
   *
   * @param id The ID of the comment to be deleted.
   * @throws NotFoundException If the comment with the provided ID is not found. {@code @override}
   */
  @Override
  public void deleteById(Integer id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    Comment comment =
        optionalComment.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, id)));
    commentRepository.delete(comment);
  }

  /**
   * This method is used to read all comments with pagination.
   *
   * @param pageRequest An instance of PageRequest which contains the pagination information.
   * @return Page<CommentDto> Returns a page of CommentDto instances.
   * @throws IllegalArgumentException If the provided PageRequest is null. {@code @override}
   */
  @Override
  public Page<CommentDto> readAll(PageRequest pageRequest) {
    Page<Comment> commentPage = commentRepository.findAll(pageRequest);
    return commentPage.map(commentMapper::toCommentDto);
  }

  /**
   * This method is used to search for comments based on a text and a list of fields.
   *
   * @param text The text to be searched in the comments.
   * @param fields The fields in the Comment class to be searched. If this list is empty, all fields
   *     annotated with FullTextField in the Comment class will be searched.
   * @param pageRequest An instance of PageRequest which contains the pagination information.
   * @return Page<CommentDto> Returns a page of CommentDto instances that match the search criteria.
   * @throws IllegalArgumentException If any of the provided fields is not a field of the Comment
   *     class annotated with FullTextField. {@code @override}
   */
  @Override
  public Page<CommentDto> search(String text, List<String> fields, PageRequest pageRequest) {
    List<String> searchableFields =
        ReflectionUtil.getAnnotatedFieldNames(Comment.class, FullTextField.class);

    List<String> fieldsToSearchBy = fields.isEmpty() ? searchableFields : fields;

    boolean containsInvalidField =
        fieldsToSearchBy.stream().anyMatch(field -> !searchableFields.contains(field));

    if (containsInvalidField) {
      throw new IllegalArgumentException();
    }

    Page<Comment> commentPage =
        commentRepository.searchBy(text, pageRequest, fieldsToSearchBy.toArray(new String[0]));
    return commentPage.map(commentMapper::toCommentDto);
  }

  /**
   * This method reads all comments that match the provided filter.
   *
   * @param commentsFilter The filter to apply when reading comments.
   * @param pageRequest The pagination information.
   * @return A page of CommentDto objects that match the filter. {@code @override}
   */
  @Override
  public Page<CommentDto> readAllWithFilter(
      CommentsFilter commentsFilter, PageRequest pageRequest) {
    Specification<Comment> spec = CommentSpecification.matchesFilter(commentsFilter);
    Page<Comment> comments = commentRepository.findAll(spec, pageRequest);
    return comments.map(commentMapper::toCommentDto);
  }
}
