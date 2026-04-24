package com.spring.backend.modules.semester;

import com.spring.backend.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "tbl_semester")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Semester extends BaseEntity {
  @Column(nullable = false, unique = true)
  String code;

  @Column(nullable = false, unique = true)
  String name;

  String description;

  @Column(nullable = false)
  Instant startDate;

  @Column(nullable = false)
  Instant endDate;

  @Column(nullable = false)
  Integer semesterOrder;

  @Column(nullable = false)
  boolean current;

  @OneToMany(mappedBy = "semester", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  Set<SemesterMember> semesterMembers = new HashSet<>();
}
