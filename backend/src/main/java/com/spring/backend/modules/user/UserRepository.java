package com.spring.backend.modules.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  /**
   * Checks if a user exists by their username.
   *
   * @param username the username to check
   * @return true if the user exists, false otherwise
   */
  boolean existsByUsername(String username);

  /**
   * Finds a user by their username.
   *
   * @param username the username to search for
   * @return an Optional containing the found user, or empty if not found
   */
  Optional<User> findByUsername(String username);

  /**
   * Finds all users associated with a specific semester ID.
   *
   * @param semesterId the ID of the semester
   * @return a list of users associated with the given semester ID
   */
  List<User> findAllBySemesterId(Long semesterId);
}
