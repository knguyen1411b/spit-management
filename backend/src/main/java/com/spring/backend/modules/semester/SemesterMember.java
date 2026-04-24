package com.spring.backend.modules.semester;

import com.spring.backend.modules.member.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "tbl_semester_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SemesterMember {

  @EmbeddedId SemesterMemberId id;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("semesterId")
  @JoinColumn(name = "semester_id")
  Semester semester;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("memberId")
  @JoinColumn(name = "member_id")
  Member member;
}
