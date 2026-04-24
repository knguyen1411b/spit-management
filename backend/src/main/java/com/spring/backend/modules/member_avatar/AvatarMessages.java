package com.spring.backend.modules.member_avatar;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AvatarMessages {
  UPLOAD_SUCCESS("Tải ảnh đại diện lên thành công"),
  UPLOAD_FAILED("Tải ảnh đại diện lên thất bại");

  String message;
}
