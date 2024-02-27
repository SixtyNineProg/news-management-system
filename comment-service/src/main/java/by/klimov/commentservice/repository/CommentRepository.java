package by.klimov.commentservice.repository;

import by.klimov.commentservice.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends SearchRepository<Comment, Integer> {}
