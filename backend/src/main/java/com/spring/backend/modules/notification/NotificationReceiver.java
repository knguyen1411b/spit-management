package com.spring.backend.modules.notification;

import com.spring.backend.modules.user.User;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "tbl_notification_receiver",
    uniqueConstraints = @UniqueConstraint(columnNames = {"notification_id", "receiver_id"}))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationReceiver {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "notification_id", nullable = false)
  Notification notification;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id", nullable = false)
  User receiver;

  @Builder.Default Boolean isRead = false;

  @Column(name = "read_at")
  Instant readAt;
}
