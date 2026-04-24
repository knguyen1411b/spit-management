package com.spring.backend.modules.task;

import com.spring.backend.modules.task.dto.TaskFilterDTO;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
  public static Specification<Task> build(TaskFilterDTO filter, Long semesterId) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(cb.equal(root.get("semester").get("id"), semesterId));

      if (filter.getKeyword() != null && !filter.getKeyword().isBlank()) {
        String keyword = "%" + filter.getKeyword().toLowerCase() + "%";
        predicates.add(
            cb.or(
                cb.like(cb.lower(root.get("title")), keyword),
                cb.like(cb.lower(root.get("description")), keyword)));
      }
      if (filter.getType() != null) {
        predicates.add(cb.equal(root.get("type"), filter.getType()));
      }
      if (filter.getFromDate() != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.getFromDate()));
      }

      if (filter.getToDate() != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.getToDate()));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}
