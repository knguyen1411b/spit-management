package com.spring.backend.modules.auth.token;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  /**
   * Finds a refresh token by its token string.
   *
   * @param refreshToken the token string to search for
   * @return an Optional containing the found RefreshToken, or empty if not found
   */
  Optional<RefreshToken> findByToken(String refreshToken);

  /**
   * Counts the number of refresh tokens associated with a given username.
   *
   * @param username the username to count refresh tokens for
   * @return the count of refresh tokens associated with the username
   */
  long countByUsername(String username);

  /**
   * Finds a refresh token for a specific username and user agent.
   *
   * @param username the username to search for
   * @param userAgent the user agent to match
   * @return an Optional containing the found RefreshToken, or empty if none found
   */
  Optional<RefreshToken> findRefreshTokenByUsernameAndUserAgent(String username, String userAgent);

  /**
   * Finds the oldest refresh token for a given username based on creation time. Useful for deleting
   * the oldest token when a user has reached the maximum allowed tokens.
   *
   * @param username the username to search for
   * @return an Optional containing the oldest RefreshToken, or empty if none exist
   */
  Optional<RefreshToken> findFirstByUsernameOrderByCreatedAtAsc(String username);
}
