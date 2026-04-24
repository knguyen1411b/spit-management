package com.spring.backend.modules.auth;

import com.spring.backend.common.constants.JwtProperties;
import com.spring.backend.common.constants.MessageConstants;
import com.spring.backend.exception.AppException;
import com.spring.backend.modules.auth.dto.*;
import com.spring.backend.modules.auth.token.RefreshToken;
import com.spring.backend.modules.auth.token.RefreshTokenRepository;
import com.spring.backend.modules.member.Member;
import com.spring.backend.modules.member.MemberRepository;
import com.spring.backend.modules.member_avatar.AvatarMessages;
import com.spring.backend.modules.member_avatar.CloudinaryService;
import com.spring.backend.modules.user.CustomUserDetails;
import com.spring.backend.modules.user.User;
import com.spring.backend.modules.user.UserRepository;
import com.spring.backend.modules.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

  JwtService jwtService;
  CloudinaryService cloudinaryService;
  UserRepository userRepository;
  UserService userService;
  PasswordEncoder passwordEncoder;
  MemberRepository memberRepository;
  RefreshTokenRepository refreshTokenRepository;
  JwtProperties jwtProperties;

  /**
   * Authenticate user with username and password. Generate new access & refresh tokens. Save
   * refresh token (with rotation & limit per user).
   *
   * @param loginRequest login DTO containing username & password
   * @param userAgent client identifier (browser/device info)
   * @return TokenDTO containing accessToken, refreshToken, and expiration times
   */
  @Transactional
  public TokenDTO login(LoginDTO loginRequest, String userAgent) {
    User user =
        userRepository
            .findByUsername(loginRequest.getUsername())
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.UNAUTHORIZED, AuthMessages.USER_LOGIN_FAILED.getMessage()));

    // verify password
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new AppException(HttpStatus.UNAUTHORIZED, AuthMessages.USER_LOGIN_FAILED.getMessage());
    }

    validateUserEnabled(user);

    // build CustomUserDetails for token generation
    CustomUserDetails userDetails = userService.loadUserByUsername(user.getUsername());

    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    // store refresh token
    saveRefreshToken(user, refreshToken, userAgent);

    return TokenDTO.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpiration(jwtProperties.getAccessTokenExpiration())
        .refreshTokenExpiration(jwtProperties.getRefreshTokenExpiration())
        .build();
  }

  /**
   * Refresh token flow: - Validate old refresh token - Delete old token from DB - Generate new
   * access & refresh tokens - Store new refresh token
   *
   * @param request DTO containing refresh token
   * @param userAgent client identifier
   * @return TokenDTO with new tokens
   */
  @Transactional
  public TokenDTO refreshToken(RefreshTokenDTO request, String userAgent) {

    String refreshTokenValue = request.getRefreshToken();

    RefreshToken storedToken =
        refreshTokenRepository
            .findByToken(refreshTokenValue)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.UNAUTHORIZED, AuthMessages.INVALID_TOKEN.getMessage()));

    String username;
    try {
      username = jwtService.extractUsername(refreshTokenValue);
    } catch (ExpiredJwtException ex) {
      refreshTokenRepository.delete(storedToken);
      throw new AppException(HttpStatus.UNAUTHORIZED, AuthMessages.TOKEN_EXPIRED.getMessage());
    }

    CustomUserDetails userDetails = userService.loadUserByUsername(username);
    validateUserEnabled(userDetails);

    if (!jwtService.isRefreshToken(refreshTokenValue)) {
      throw new AppException(HttpStatus.UNAUTHORIZED, AuthMessages.INVALID_TOKEN.getMessage());
    }

    refreshTokenRepository.delete(storedToken);

    String newAccessToken = jwtService.generateAccessToken(userDetails);
    String newRefreshToken = jwtService.generateRefreshToken(userDetails);

    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.UNAUTHORIZED, AuthMessages.INVALID_TOKEN.getMessage()));

    saveRefreshToken(user, newRefreshToken, userAgent);

    return TokenDTO.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .accessTokenExpiration(jwtProperties.getAccessTokenExpiration())
        .refreshTokenExpiration(jwtProperties.getRefreshTokenExpiration())
        .build();
  }

  /**
   * Get the profile of the currently authenticated user.
   *
   * @return ProfileDTO containing user info, roles, and permissions
   */
  @Transactional
  public ProfileDTO getProfile() {
    CustomUserDetails userDetails =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    User user =
        userRepository
            .findById(userDetails.getId())
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.UNAUTHORIZED,
                        MessageConstants.INTERNAL_SERVER_ERROR.getMessage()));

    validateUserEnabled(user);

    Member member = memberRepository.findByUsername(user.getUsername()).orElse(null);

    return ProfileDTO.builder()
        .id(user.getId())
        .username(user.getUsername())
        .enabled(user.isEnabled())
        .superuser(user.isSuperuser())
        .passwordChanged(user.isPasswordChanged())
        .semesterId(user.getSemesterId())
        .permissions(
            userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
        .avatar(member != null ? member.getAvatar() : null)
        .firstName(member != null ? member.getFirstName() : null)
        .lastName(member != null ? member.getLastName() : null)
        .className(member != null ? member.getClassName() : null)
        .birthday(member != null ? member.getBirthday() : null)
        .email(member != null ? member.getEmail() : null)
        .phone(member != null ? member.getPhone() : null)
        .gender(member != null ? member.isGender() : null)
        .generation(member != null ? member.getGeneration() : null)
        .description(member != null ? member.getDescription() : null)
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .build();
  }

  /**
   * Change password of the currently authenticated user. Validates old password before updating.
   *
   * @param changePass DTO containing oldPassword & newPassword
   */
  @Transactional
  public void changePassword(ChangePasswordDTO changePass) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    User user =
        userRepository
            .findByUsername(auth.getName())
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.UNAUTHORIZED,
                        MessageConstants.INTERNAL_SERVER_ERROR.getMessage()));

    validateUserEnabled(user);

    // check old password
    if (!passwordEncoder.matches(changePass.getOldPassword(), user.getPassword())) {
      throw new AppException(
          HttpStatus.UNAUTHORIZED, AuthMessages.INVALID_OLD_PASSWORD.getMessage());
    }

    // update password
    user.setPassword(passwordEncoder.encode(changePass.getNewPassword()));

    user.setPasswordChanged(true);

    userRepository.save(user);
  }

  /**
   * Save refresh token in DB with rotation and limit: - Delete old token for same user & userAgent
   * - Keep max 5 tokens per user (delete oldest if exceed)
   *
   * @param user the authenticated user
   * @param token new refresh token value
   * @param userAgent client identifier
   */
  @Transactional
  public void saveRefreshToken(User user, String token, String userAgent) {
    // delete existing token for this user-agent
    refreshTokenRepository
        .findRefreshTokenByUsernameAndUserAgent(user.getUsername(), userAgent)
        .ifPresent(refreshTokenRepository::delete);

    // limit 5 tokens per user
    long tokenCount = refreshTokenRepository.countByUsername(user.getUsername());
    if (tokenCount >= 5) {
      refreshTokenRepository
          .findFirstByUsernameOrderByCreatedAtAsc(user.getUsername())
          .ifPresent(refreshTokenRepository::delete);
    }

    RefreshToken newToken =
        RefreshToken.builder()
            .username(user.getUsername())
            .token(token)
            .userAgent(userAgent)
            .createdAt(Instant.now())
            .build();
    refreshTokenRepository.save(newToken);
  }

  /**
   * Upload avatar of the member linked to the given username.
   *
   * @param username the username of the member
   * @param file the avatar file to upload
   * @throws AppException if the member is not found or if an error occurs during upload
   */
  @Transactional
  public void uploadAvatar(String username, MultipartFile file) {
    Member member =
        memberRepository
            .findByUsername(username)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, AuthMessages.USER_NOT_LINK_MEMBER.getMessage()));
    try {
      String avatarUrl = cloudinaryService.uploadAvatar(file, member.getId());
      member.setAvatar(avatarUrl);
      memberRepository.save(member);
    } catch (Exception e) {
      throw new AppException(
          HttpStatus.INTERNAL_SERVER_ERROR, AvatarMessages.UPLOAD_FAILED.getMessage());
    }
  }

  /**
   * Validate that user is enabled. Throw exception if disabled.
   *
   * @param user domain user entity
   */
  static void validateUserEnabled(User user) {
    if (!user.isEnabled()) {
      throw new AppException(HttpStatus.UNAUTHORIZED, AuthMessages.USER_NOT_ENABLED.getMessage());
    }
  }

  /**
   * Validate that user (UserDetails) is enabled. Throw exception if disabled.
   *
   * @param user spring security user details
   */
  static void validateUserEnabled(UserDetails user) {
    if (!user.isEnabled()) {
      throw new AppException(HttpStatus.UNAUTHORIZED, AuthMessages.USER_NOT_ENABLED.getMessage());
    }
  }
}
