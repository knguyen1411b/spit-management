package com.spring.backend.modules.member;

import com.spring.backend.common.entity.BaseEntity;
import com.spring.backend.modules.semester.SemesterMember;
import com.spring.backend.modules.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "tbl_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Member extends BaseEntity {
  @Column(nullable = false)
  String lastName;

  @Column(nullable = false)
  String firstName;

  @Column(nullable = false)
  boolean gender;

  @Column(nullable = false)
  Instant birthday;

  @Column(nullable = false, unique = true)
  String email;

  @Column(nullable = false)
  String phone;

  @Column(nullable = false)
  String className;

  @Column String avatar;

  @Column(nullable = false)
  String generation;

  @Column(unique = true)
  String username;

  @Column String description;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  User user;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  Set<SemesterMember> semesterMembers = new HashSet<>();
}
