package com.spring.backend.common.validation.validators;

import com.spring.backend.common.validation.constraints.RolesExist;
import com.spring.backend.modules.role.RoleRepository;
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
public class RolesExistValidator implements ConstraintValidator<RolesExist, List<Long>> {
  RoleRepository roleRepository;

  @Override
  public boolean isValid(List<Long> roleIds, ConstraintValidatorContext context) {
    if (roleIds == null || roleIds.isEmpty()) return true;
    return roleRepository.countByIdIn(roleIds) == roleIds.size();
  }
}
