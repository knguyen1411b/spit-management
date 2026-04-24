package com.spring.backend.modules.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "User Filter Data Transfer Object")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterDTO {
  String keyword;
  String enabled;
  String superuser;
  String roleName;
}
