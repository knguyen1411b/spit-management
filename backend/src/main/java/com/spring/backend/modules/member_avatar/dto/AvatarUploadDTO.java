package com.spring.backend.modules.member_avatar.dto;

import com.spring.backend.common.validation.constraints.ValidImage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(description = "DTO for uploading avatar in base64 format")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvatarUploadDTO {
  @Schema(description = "Avatar image file to upload")
  @ValidImage
  @NotNull(message = "Ảnh đại diện không được để trống")
  MultipartFile avatarFile;
}
