package com.spring.backend.modules.notification;

import com.spring.backend.common.entity.BaseEntity;
import com.spring.backend.modules.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "tbl_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends BaseEntity {
  @Column(nullable = false)
  String title;

  @Column(nullable = false, columnDefinition = "TEXT")
  String content;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  NotificationType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id", nullable = false)
  User sender;
}
