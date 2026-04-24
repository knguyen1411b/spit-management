package com.spring.backend.modules.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
  /**
   * Retrieves a paginated list of boards whose {@code name} and {@code description} contain the
   * given search keywords, ignoring case.
   *
   * @param name the partial name to search for (case-insensitive)
   * @param description the partial description to search for (case-insensitive)
   * @param pageable pagination and sorting information
   * @return a {@link Page} of {@link Board} entities matching the criteria
   */
  Page<Board> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
      String name, String description, Pageable pageable);

  /**
   * Checks if a board with the specified name exists.
   *
   * @param name the name of the board to check
   * @return {@code true} if a board with the given name exists, {@code false} otherwise
   */
  boolean existsByName(String name);
}
