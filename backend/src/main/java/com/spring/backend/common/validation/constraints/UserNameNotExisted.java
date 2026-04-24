package com.spring.backend.common.validation.constraints;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;

import com.spring.backend.common.validation.validators.UserNameNotExistedValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameNotExistedValidator.class)
public @interface UserNameNotExisted {
  String message() default "Tên đăng nhập đã tồn tại";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
