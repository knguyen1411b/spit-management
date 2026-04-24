package com.spring.backend.common.validation.validators;

import com.spring.backend.common.validation.constraints.UserNameNotExisted;
import com.spring.backend.modules.user.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserNameNotExistedValidator
    implements ConstraintValidator<UserNameNotExisted, String> {
  UserRepository userRepository;

  @Override
  public boolean isValid(String username, ConstraintValidatorContext context) {
    if (username == null || username.trim().isEmpty()) {
      return true;
    }

    return !userRepository.existsByUsername(username);
  }
}
