package com.spring.backend.modules.auth;

import com.spring.backend.common.constants.JwtProperties;
import com.spring.backend.exception.AppException;
import com.spring.backend.modules.user.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service class for handling JWT generation and validation. Responsibilities: - Generate Access &
 * Refresh tokens for authenticated users - Extract information (subject, claims) from token -
 * Validate token (expiration, subject match)
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtService {

  final JwtProperties jwtProperties;

  /**
   * Builds the secret key object from the configured secret string.
   *
   * @return SecretKey used for signing/verifying JWT
   */
  SecretKey getSignKey() {
    return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  /**
   * Generates a new Access Token for the given user.
   *
   * @param userDetails authenticated user
   * @return signed JWT string (access token)
   */
  public String generateAccessToken(CustomUserDetails userDetails) {
    return buildToken(userDetails.getUsername(), jwtProperties.getAccessTokenExpiration(), false);
  }

  /**
   * Generates a new Refresh Token for the given user.
   *
   * @param userDetails authenticated user
   * @return signed JWT string (refresh token)
   */
  public String generateRefreshToken(CustomUserDetails userDetails) {
    return buildToken(userDetails.getUsername(), jwtProperties.getRefreshTokenExpiration(), true);
  }

  /**
   * Helper method to build JWT with given subject and expiration time.
   *
   * @param subject subject of token (usually username or userId)
   * @param expiration expiration time in milliseconds
   * @param isRefresh whether the token is a refresh token
   * @return signed JWT string
   */
  String buildToken(String subject, long expiration, boolean isRefresh) {
    return Jwts.builder()
        .setSubject(subject)
        .claim("typ", isRefresh ? "refresh" : "access")
        .setId(UUID.randomUUID().toString().replace("-", "").substring(0, 12))
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSignKey(), SignatureAlgorithm.HS512)
        .compact();
  }

  /**
   * Validate the token for a specific user.
   *
   * @param token JWT token string
   * @param userDetails expected user
   * @return true if token belongs to user and is not expired
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  /**
   * Extract the subject username from the token.
   *
   * @param token JWT token string
   * @return subject stored in token
   */
  public String extractUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  /**
   * Check if a token is expired.
   *
   * @param token JWT token string
   * @return true if expired, false otherwise
   */
  boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  /**
   * Extract all claims from a token.
   *
   * @param token JWT token string
   * @return Claims object (contains sub, exp, jti, etc.)
   * @throws AppException if token is invalid/expired
   */
  Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
  }

  /**
   * Extract the token type (access/refresh) from the token claims.
   *
   * @param token JWT token string
   * @return token type as string
   */
  public String extractTokenType(String token) {
    return extractAllClaims(token).get("typ", String.class);
  }

  /**
   * Check if the token is an Access Token.
   *
   * @param token JWT token string
   * @return true if access token, false otherwise
   */
  public boolean isAccessToken(String token) {
    return "access".equals(extractTokenType(token));
  }

  /**
   * Check if the token is a Refresh Token.
   *
   * @param token JWT token string
   * @return true if refresh token, false otherwise
   */
  public boolean isRefreshToken(String token) {
    return "refresh".equals(extractTokenType(token));
  }
}
