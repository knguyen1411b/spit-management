package com.spring.backend.common.validation.constraints;

import static java.lang.annotation.ElementType.*;

import com.spring.backend.common.validation.validators.MembersExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MembersExistValidator.class)
public @interface MembersExist {
  String message() default "Một hoặc nhiều thành viên không tồn tại.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
