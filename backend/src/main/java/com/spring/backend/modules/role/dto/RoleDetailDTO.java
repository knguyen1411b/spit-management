package com.spring.backend.modules.role.dto;

import com.spring.backend.modules.permission.dto.PermissionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(description = "Detailed Role DTO with associated permissions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDetailDTO extends RoleDTO {
  @Schema(description = "List of permissions associated with the role")
  List<PermissionDTO> permissions;
}
