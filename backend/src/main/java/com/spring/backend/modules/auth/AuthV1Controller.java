package com.spring.backend.modules.auth;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.auth.dto.*;
import com.spring.backend.modules.member_avatar.AvatarMessages;
import com.spring.backend.modules.member_avatar.dto.AvatarUploadDTO;
import com.spring.backend.modules.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "04. Auth (V1)", description = "Authentication and Authorization APIs")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthV1Controller {
  AuthService authService;

  /**
   * @param loginDTO the login data transfer object containing username and password
   * @param httpRequest the HTTP request to extract user agent information
   * @return a DataApiResponse containing a TokenDTO on successful login
   */
  @Operation(
      summary = "Login",
      description =
          "Authenticate a user with username and password. Returns a token if successful.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User login in successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = DataApiResponseTokenDTO.class))),
      })
  @BadRequestApiResponse
  @SecurityRequirements
  @PostMapping("/login")
  public DataApiResponse<TokenDTO> login(
      @RequestBody LoginDTO loginDTO, HttpServletRequest httpRequest) {
    String userAgent = httpRequest.getHeader("User-Agent");
    return DataApiResponse.success(
        authService.login(loginDTO, userAgent), AuthMessages.LOGIN_SUCCESS.getMessage());
  }

  /**
   * @param tokenDTO the refresh token data transfer object containing the refresh token
   * @param httpRequest the HTTP request to extract user agent information
   * @return a DataApiResponse containing a TokenDTO on successful token refresh
   */
  @Operation(
      summary = "Refresh Token",
      description =
          "Refresh the authentication token using a valid refresh token. Returns a new token if successful.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Token refreshed successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = DataApiResponseTokenDTO.class))),
      })
  @BadRequestApiResponse
  @SecurityRequirements
  @PostMapping("/refresh-token")
  public DataApiResponse<TokenDTO> refreshToken(
      @RequestBody RefreshTokenDTO tokenDTO, HttpServletRequest httpRequest) {
    String userAgent = httpRequest.getHeader("User-Agent");
    return DataApiResponse.success(
        authService.refreshToken(tokenDTO, userAgent),
        AuthMessages.REFRESH_TOKEN_SUCCESS.getMessage());
  }

  /**
   * @return a DataApiResponse containing the user's profile information
   */
  @Operation(
      summary = "Get Profile",
      description =
          "Fetch the profile information of the authenticated user. Returns user details if successful.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Profile fetched successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = DataApiResponseProfileDTO.class))),
      })
  @BadRequestApiResponse
  @GetMapping("/profile")
  public DataApiResponse<ProfileDTO> profile() {
    return DataApiResponse.success(
        authService.getProfile(), AuthMessages.PROFILE_FETCH_SUCCESS.getMessage());
  }

  /**
   * @param changePass the change password data transfer object containing old and new passwords
   * @return an ApiResponse indicating success or failure of the password change operation
   */
  @Operation(
      summary = "Change Password",
      description =
          "Change the password of the authenticated user. Requires old and new passwords.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Password changed successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @BadRequestApiResponse
  @PostMapping("/change-password")
  public ApiResponse changePassword(@RequestBody ChangePasswordDTO changePass) {
    authService.changePassword(changePass);
    return ApiResponse.success(AuthMessages.PASSWORD_CHANGE_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Upload an avatar",
      description = "Uploads an avatar image for the authenticated user.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Avatar uploaded successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PostMapping(value = "/avatar", consumes = "multipart/form-data")
  public ApiResponse upload(
      @Valid @ModelAttribute AvatarUploadDTO avatarUploadDTO,
      @AuthenticationPrincipal CustomUserDetails user) {
    authService.uploadAvatar(user.getUsername(), avatarUploadDTO.getAvatarFile());
    return ApiResponse.success(AvatarMessages.UPLOAD_SUCCESS.getMessage());
  }

  public static class DataApiResponseTokenDTO extends DataApiResponse<TokenDTO> {}

  public static class DataApiResponseProfileDTO extends DataApiResponse<ProfileDTO> {}
}
