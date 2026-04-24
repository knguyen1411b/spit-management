package com.spring.backend.common.validation.constraints;

import static java.lang.annotation.ElementType.*;

import com.spring.backend.common.validation.validators.RolesExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RolesExistValidator.class)
public @interface RolesExist {
  String message() default "Một hoặc nhiều vai trò không tồn tại";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
