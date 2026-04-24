package com.spring.backend.modules.user;

import com.spring.backend.common.entity.BaseEntity;
import com.spring.backend.modules.role.Role;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
  @Column(name = "username", nullable = false, unique = true)
  String username;

  @Column(name = "password", nullable = false)
  String password;

  @Column(name = "superuser", nullable = false)
  @Builder.Default
  boolean superuser = false;

  @Column(name = "password_changed", nullable = false)
  @Builder.Default
  boolean passwordChanged = false;

  @Column(name = "enabled", nullable = false)
  @Builder.Default
  boolean enabled = true;

  @Column(name = "semester_id", nullable = false)
  long semesterId;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "tbl_user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @Builder.Default
  Set<Role> roles = new HashSet<>();
}
