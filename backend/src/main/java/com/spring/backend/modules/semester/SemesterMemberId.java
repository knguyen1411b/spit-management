package com.spring.backend.modules.semester;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterMemberId implements Serializable {

  @Column(name = "semester_id")
  Long semesterId;

  @Column(name = "member_id")
  Long memberId;
}
