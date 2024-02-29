package by.klimov.commentservice.service.impl;

import by.klimov.commentservice.dto.CommentDto;
import by.klimov.commentservice.entity.Comment;
import by.klimov.commentservice.exception.NotFoundException;
import by.klimov.commentservice.mapper.CommentMapper;
import by.klimov.commentservice.repository.CommentRepository;
import by.klimov.commentservice.service.CommentService;
import by.klimov.commentservice.util.ReflectionUtil;
import java.util.List;
import java.util.Optional;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

  public static final String ERROR_FORMAT_NOT_FOUND = "Comment with uuid = %s not found";

  private final CommentRepository commentRepository;
  private final CommentMapper commentMapper;

  public CommentServiceImpl(CommentRepository commentRepository) {
    this.commentRepository = commentRepository;
    this.commentMapper = CommentMapper.INSTANCE;
  }

  @Override
  public CommentDto create(CommentDto commentDto) {
    Comment comment = commentMapper.toComment(commentDto);
    Comment repositoryComment = commentRepository.save(comment);
    return commentMapper.toCommentDto(repositoryComment);
  }

  @Override
  public Optional<CommentDto> readById(Integer id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    return optionalComment.map(commentMapper::toCommentDto);
  }

  @Override
  public CommentDto update(CommentDto commentDto) {
    Optional<Comment> optionalComment = commentRepository.findById(commentDto.id());
    Comment comment =
        optionalComment.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, commentDto.id())));
    comment = commentMapper.merge(commentDto, comment);
    return commentMapper.toCommentDto(commentRepository.save(comment));
  }

  @Override
  public void deleteById(Integer id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    Comment comment =
        optionalComment.orElseThrow(
            () -> new NotFoundException(String.format(ERROR_FORMAT_NOT_FOUND, id)));
    commentRepository.delete(comment);
  }

  @Override
  public Page<CommentDto> readAll(PageRequest pageRequest) {
    Page<Comment> commentPage = commentRepository.findAll(pageRequest);
    return commentPage.map(commentMapper::toCommentDto);
  }

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
}
