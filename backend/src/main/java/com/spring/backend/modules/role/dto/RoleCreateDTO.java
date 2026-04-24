package com.spring.backend.modules.role.dto;

import com.spring.backend.common.validation.constraints.PermissionsExist;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a new role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateDTO {
  @Schema(description = "Unique code of the role", example = "ADMIN")
  @NotBlank(message = "Mã vai trò không được để trống")
  String code;

  @Schema(description = "Name of the role", example = "Administrator")
  @NotBlank(message = "Tên vai trò không được để trống")
  String name;

  @Schema(description = "Description of the role", example = "Full access to all resources")
  @NotNull(message = "Mô tả không được để trống")
  String description;

  @PermissionsExist
  @Schema(description = "List of permission IDs associated with the role", example = "[1, 2, 3]")
  @NotNull(message = "Danh sách quyền không được để trống")
  List<Long> permissionIds;
}
