package com.spring.backend.modules.notification.dto;

import com.spring.backend.modules.notification.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Schema(description = "Data Transfer Object for Notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDTO {
  @Schema(description = "Unique identifier of the notification", example = "1")
  Long id;

  @Schema(description = "Title of the notification", example = "System Maintenance")
  String title;

  @Schema(
      description = "Content of the notification",
      example = "The system will be down for maintenance on Saturday at 2 AM.")
  String content;

  @Schema(description = "Type of the notification", example = "INFO, ALERT, WARNING")
  NotificationType type;

  @Schema(description = "Indicates whether the notification has been read", example = "false")
  boolean isRead;

  @Schema(
      description = "Timestamp when the notification was created",
      example = "2024-06-15T10:15:30Z")
  Instant createdAt;
}
