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
@Schema(description = "Data Transfer Object for updating an existing role")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateDTO {
  @Schema(description = "Name of the role", example = "Administrator")
  @NotBlank(message = "Tên vai trò không được để trống")
  String name;

  @Schema(description = "Description of the role", example = "Full access to all resources")
  String description;

  @Schema(description = "List of permission IDs associated with the role", example = "[1, 2, 3]")
  @PermissionsExist
  @NotNull(message = "Danh sách quyền không được để trống")
  List<Long> permissionIds;
}
