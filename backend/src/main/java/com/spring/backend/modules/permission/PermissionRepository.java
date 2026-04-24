package com.spring.backend.modules.permission;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Permission} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations, and includes additional
 * query methods for searching and filtering permissions.
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

  /**
   * Retrieves a paginated list of permissions whose {@code name} and {@code title} contain the
   * given search keywords, ignoring case.
   *
   * @param code the partial code to search for (case-insensitive)
   * @param name the partial name to search for (case-insensitive)
   * @param description the partial description to search for (case-insensitive)
   * @param pageable pagination and sorting information
   * @return a {@link Page} of {@link Permission} entities matching the criteria
   */
  Page<Permission> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContaining(
      String code, String name, String description, Pageable pageable);

  /**
   * Counts the number of permissions whose IDs are in the specified list.
   *
   * @param permissionIds the list of permission IDs to count
   * @return the number of permissions found
   */
  Long countByIdIn(List<Long> permissionIds);
}
