package com.spring.backend.modules.permission;

import com.spring.backend.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "tbl_permission")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {
  @Column(nullable = false, unique = true)
  String code;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  String description;
}
