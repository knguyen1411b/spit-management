package com.spring.backend.modules.role;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  /**
   * Retrieves a paginated list of {@link Role} entities whose name and title contain the specified
   * substrings, ignoring case considerations.
   *
   * @param code the substring to search for within the role's code (case-insensitive)
   * @param name the substring to search for within the role's name (case-insensitive)
   * @param description the substring to search for within the role's description (case-insensitive)
   * @param pageable the pagination information
   * @return a {@link Page} of {@link Role} entities matching the search criteria
   */
  Page<Role> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContaining(
      String code, String name, String description, Pageable pageable);

  /**
   * Checks if a role with the specified code exists.
   *
   * @param code the code of the role to check
   * @return true if a role with the specified code exists, false otherwise
   */
  boolean existsByCode(String code);

  /**
   * Finds a role by its name.
   *
   * @param name the name of the role to find
   * @return an {@link Optional} containing the found role, or empty if no role with the given name
   *     exists
   */
  Optional<Role> findByName(String name);

  /**
   * Counts the number of roles with the specified IDs.
   *
   * @param roleIds the list of role IDs to count
   * @return the number of roles with the specified IDs
   */
  Long countByIdIn(List<Long> roleIds);
}
