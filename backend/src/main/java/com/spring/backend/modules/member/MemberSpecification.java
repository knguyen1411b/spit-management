package com.spring.backend.modules.member;

import com.spring.backend.modules.member.dto.MemberFilterDTO;
import com.spring.backend.modules.user.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class MemberSpecification {

  /**
   * Builds a composite Specification based on the provided MemberFilterDTO. Each filter criterion
   * is combined using logical AND.
   *
   * @param filter the MemberFilterDTO containing filter criteria
   * @return a Specification for filtering members
   */
  public static Specification<Member> build(MemberFilterDTO filter) {
    return Specification.allOf(
        hasFullName(filter.getFullName()),
        hasGender(filter.getGender()),
        hasEmail(filter.getEmail()),
        hasPhone(filter.getPhone()),
        hasClassName(filter.getClassName()),
        hasGeneration(filter.getGeneration()),
        hasDescription(filter.getDescription()),
        hasUsername(filter.getUsername()),
        hasSemester(filter.getSemesterId()));
  }

  /**
   * Specification to filter members by semester ID.
   *
   * @param semesterId the semester ID to filter by
   * @return a Specification for filtering by semester ID
   */
  public static Specification<Member> hasSemester(Long semesterId) {
    return (root, query, cb) -> {
      if (semesterId == null) return cb.conjunction();

      if (query != null) query.distinct(true);

      Join<?, ?> smJoin = root.join("semesterMembers", JoinType.INNER);
      return cb.equal(smJoin.get("semester").get("id"), semesterId);
    };
  }

  /**
   * Specification to filter members by full name (either first name or last name).
   *
   * @param fullName the full name to filter by
   * @return a Specification for filtering by full name
   */
  public static Specification<Member> hasFullName(String fullName) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(fullName)) return cb.conjunction();
      Path<String> lastNamePath = root.get("lastName");
      Path<String> firstNamePath = root.get("firstName");
      String pattern = "%" + fullName.toLowerCase() + "%";
      return cb.or(
          cb.like(cb.lower(lastNamePath), pattern), cb.like(cb.lower(firstNamePath), pattern));
    };
  }

  /**
   * Specification to filter members by gender.
   *
   * @param gender the gender to filter by
   * @return a Specification for filtering by gender
   */
  public static Specification<Member> hasGender(Boolean gender) {
    return (root, query, cb) -> {
      if (gender == null) return cb.conjunction();
      Path<Boolean> path = root.get("gender");
      return cb.equal(path, gender);
    };
  }

  /**
   * Specification to filter members by email.
   *
   * @param email the email to filter by
   * @return a Specification for filtering by email
   */
  public static Specification<Member> hasEmail(String email) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(email)) return cb.conjunction();
      Path<String> path = root.get("email");
      return cb.like(cb.lower(path), "%" + email.toLowerCase() + "%");
    };
  }

  /**
   * Specification to filter members by phone number.
   *
   * @param phone the phone number to filter by
   * @return a Specification for filtering by phone number
   */
  public static Specification<Member> hasPhone(String phone) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(phone)) return cb.conjunction();
      Path<String> path = root.get("phone");
      return cb.like(path, "%" + phone + "%");
    };
  }

  /**
   * Specification to filter members by class name.
   *
   * @param className the class name to filter by
   * @return a Specification for filtering by class name
   */
  public static Specification<Member> hasClassName(String className) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(className)) return cb.conjunction();
      Path<String> path = root.get("className");
      return cb.like(cb.lower(path), "%" + className.toLowerCase() + "%");
    };
  }

  /**
   * Specification to filter members by generation.
   *
   * @param generation the generation to filter by
   * @return a Specification for filtering by generation
   */
  public static Specification<Member> hasGeneration(String generation) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(generation)) return cb.conjunction();
      Path<String> path = root.get("generation");
      return cb.like(cb.lower(path), "%" + generation.toLowerCase() + "%");
    };
  }

  /**
   * Specification to filter members by description.
   *
   * @param description the description to filter by
   * @return a Specification for filtering by description
   */
  public static Specification<Member> hasDescription(String description) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(description)) return cb.conjunction();
      Path<String> path = root.get("description");
      return cb.like(cb.lower(path), "%" + description.toLowerCase() + "%");
    };
  }

  /**
   * Specification to filter members by the username of the linked user.
   *
   * @param username the username to filter by
   * @return a Specification for filtering by username
   */
  public static Specification<Member> hasUsername(String username) {
    return (root, query, cb) -> {
      if (!StringUtils.hasText(username)) return cb.conjunction();
      Join<Member, User> userJoin = root.join("user", JoinType.INNER);
      return cb.equal(cb.lower(userJoin.get("username")), username.toLowerCase());
    };
  }
}
