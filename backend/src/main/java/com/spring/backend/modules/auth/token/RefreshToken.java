package com.spring.backend.modules.auth.token;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Builder
@Getter
@Setter
@Table(name = "tbl_refresh_token")
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @Column(nullable = false)
  String username;

  @Column(nullable = false, unique = true, length = 512)
  String token;

  String userAgent;

  @CreationTimestamp
  @Column(updatable = false)
  Instant createdAt;
}
