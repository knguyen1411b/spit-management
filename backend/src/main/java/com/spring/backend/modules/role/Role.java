package com.spring.backend.modules.role;

import com.spring.backend.common.entity.BaseEntity;
import com.spring.backend.modules.permission.Permission;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "tbl_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {
  @Column(nullable = false, unique = true)
  String code;

  @Column(nullable = false)
  String name;

  @Column(nullable = false)
  String description;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "tbl_role_permission",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  @Builder.Default
  Set<Permission> permissions = new HashSet<>();
}
