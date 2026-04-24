package com.spring.backend.modules.semester;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
  /**
   * Finds semesters by name and description containing the specified keywords, ignoring case.
   *
   * @param name the keyword to search in the name
   * @param description the keyword to search in the description
   * @param pageable the pagination information
   * @return a paginated list of Semester objects matching the search criteria
   */
  Page<Semester>
      findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
          String code, String name, String description, Pageable pageable);

  /**
   * Finds the current active semester.
   *
   * @return the current Semester object, or null if no current semester is set
   */
  Semester findByCurrentTrue();

  /**
   * Checks if a semester with the given code exists, excluding the semester with the specified ID.
   *
   * @param code the code to check for existence
   * @param id the ID of the semester to exclude from the check
   * @return true if a semester with the given code exists (excluding the specified ID), false
   *     otherwise
   */
  boolean existsByCodeAndIdNot(String code, Long id);
}
