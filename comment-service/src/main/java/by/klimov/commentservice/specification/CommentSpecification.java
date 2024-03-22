package by.klimov.commentservice.specification;

import by.klimov.commentservice.entity.Comment;
import by.klimov.commentservice.model.CommentsFilter;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CommentSpecification {

  public Specification<Comment> matchesFilter(CommentsFilter filter) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (filter.getNewsId() != null) {
        predicates.add(criteriaBuilder.equal(root.get(Comment.Fields.newsId), filter.getNewsId()));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
