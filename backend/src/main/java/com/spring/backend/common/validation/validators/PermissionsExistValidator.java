package com.spring.backend.common.validation.validators;

import com.spring.backend.common.validation.constraints.PermissionsExist;
import com.spring.backend.modules.permission.PermissionRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionsExistValidator
    implements ConstraintValidator<PermissionsExist, List<Long>> {
  PermissionRepository permissionRepository;

  @Override
  public boolean isValid(List<Long> permissionIds, ConstraintValidatorContext context) {
    if (permissionIds == null || permissionIds.isEmpty()) {
      return true;
    }
    return permissionRepository.countByIdIn(permissionIds) == permissionIds.size();
  }
}
