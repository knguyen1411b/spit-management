package com.spring.backend.modules.user;

import com.spring.backend.modules.role.Role;
import com.spring.backend.modules.user.dto.UserFilterDTO;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class UserSpecification {

  /**
   * Builds a composite Specification based on the provided UserFilterDTO. Each filter criterion is
   * combined using logical AND.
   *
   * @param filter the UserFilterDTO containing filter criteria
   * @return a Specification for filtering users
   */
  public static Specification<User> build(UserFilterDTO filter) {
    return Specification.allOf(
        keywordContainingIgnoreCase(filter.getKeyword()),
        hasEnabledStatus(filter.getEnabled()),
        hasSuperuserStatus(filter.getSuperuser()),
        roleNameEqualsIgnoreCase(filter.getRoleName()));
  }

  /**
   * Creates a Specification that matches users whose username contains the given keyword, ignoring
   * case. If the keyword is null or blank, matches all users.
   *
   * @param keyword the keyword to search in the username
   * @return a Specification for filtering users by username
   */
  public static Specification<User> keywordContainingIgnoreCase(String keyword) {
    return (root, query, criteriaBuilder) -> {
      if (keyword == null || keyword.isBlank()) {
        return criteriaBuilder.conjunction();
      }
      String likePattern = "%" + keyword.toLowerCase() + "%";
      Path<String> usernamePath = root.get("username");
      return criteriaBuilder.like(criteriaBuilder.lower(usernamePath), likePattern);
    };
  }

  /**
   * Creates a Specification that matches users by their enabled status. If the status is null or
   * empty, matches all users.
   *
   * @param status "true" or "false" as a String
   * @return a Specification for filtering users by enabled status
   */
  public static Specification<User> hasEnabledStatus(String status) {
    return (root, query, criteriaBuilder) -> {
      if (!StringUtils.hasText(status)) {
        return criteriaBuilder.conjunction();
      }
      boolean isEnabled = Boolean.parseBoolean(status);
      Path<Boolean> enabledPath = root.get("enabled");
      return isEnabled ? criteriaBuilder.isTrue(enabledPath) : criteriaBuilder.isFalse(enabledPath);
    };
  }

  /**
   * Creates a Specification that matches users by their superuser status. If the status is null or
   * empty, matches all users.
   *
   * @param status "true" or "false" as a String
   * @return a Specification for filtering users by superuser status
   */
  public static Specification<User> hasSuperuserStatus(String status) {
    return (root, query, criteriaBuilder) -> {
      if (!StringUtils.hasText(status)) {
        return criteriaBuilder.conjunction();
      }
      boolean isSuperuser = Boolean.parseBoolean(status);
      Path<Boolean> superuserPath = root.get("superuser");
      return isSuperuser
          ? criteriaBuilder.isTrue(superuserPath)
          : criteriaBuilder.isFalse(superuserPath);
    };
  }

  /**
   * Creates a Specification that matches users having a role with the given name, ignoring case. If
   * the roleName is null or blank, matches all users.
   *
   * @param roleName the name of the role to match
   * @return a Specification for filtering users by role name
   */
  public static Specification<User> roleNameEqualsIgnoreCase(String roleName) {
    return (root, query, criteriaBuilder) -> {
      if (roleName == null || roleName.isBlank()) {
        return criteriaBuilder.conjunction();
      }
      Join<User, Role> rolesJoin = root.joinSet("roles", JoinType.INNER);
      return criteriaBuilder.equal(
          criteriaBuilder.lower(rolesJoin.get("name")), roleName.toLowerCase());
    };
  }
}
