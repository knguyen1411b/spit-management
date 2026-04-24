package com.spring.backend.modules.user;

import java.time.Instant;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomUserDetails implements UserDetails {
  Long id;
  String username;
  String password;

  boolean enabled;
  boolean superuser;
  boolean passwordChanged;

  long semesterId;

  List<GrantedAuthority> authorities;

  Instant createdAt;
  Instant updatedAt;

  public List<GrantedAuthority> getAuthorities() {
    return superuser
        ? List.of(new SimpleGrantedAuthority("all:all"))
        : authorities != null ? authorities : List.of();
  }
}
