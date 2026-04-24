package com.spring.backend.modules.member_avatar;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.member_avatar.dto.AvatarUploadDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "06. Member (V1)")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarV1Controller {
  AvatarService avatarService;

  @Operation(
      summary = "Upload an avatar",
      description = "Uploads an avatar for the member with the given ID.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Members retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Member not found",
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
  @PreAuthorize("hasAnyAuthority('all:all', 'member:update')")
  @PostMapping(value = "/{id}/avatar", consumes = "multipart/form-data")
  public ApiResponse upload(
      @PathVariable Long id, @Valid @ModelAttribute AvatarUploadDTO avatarUploadDTO) {
    avatarService.upload(id, avatarUploadDTO.getAvatarFile());
    return ApiResponse.success(AvatarMessages.UPLOAD_SUCCESS.getMessage());
  }
}
