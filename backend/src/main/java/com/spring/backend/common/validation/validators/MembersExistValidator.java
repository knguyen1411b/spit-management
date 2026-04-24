package com.spring.backend.common.validation.validators;

import com.spring.backend.common.validation.constraints.MembersExist;
import com.spring.backend.modules.member.MemberRepository;
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
public class MembersExistValidator implements ConstraintValidator<MembersExist, List<Long>> {
  MemberRepository memberRepository;

  @Override
  public boolean isValid(List<Long> memberIds, ConstraintValidatorContext context) {
    if (memberIds == null || memberIds.isEmpty()) {
      return true;
    }
    long foundMembers = memberRepository.countByIdIn(memberIds);
    return foundMembers == memberIds.size();
  }
}
