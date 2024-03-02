package by.klimov.commentservice.specification;

import by.klimov.commentservice.entity.Comment;
import by.klimov.commentservice.model.CommentsFilter;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentSpecification {

  public static Specification<Comment> matchesFilter(CommentsFilter filter) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (filter.getNewsId() != null) {
        predicates.add(cb.equal(root.get(Comment.Fields.newsId), filter.getNewsId()));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
