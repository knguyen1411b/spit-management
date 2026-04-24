package com.spring.backend.modules.notification.dto;

import com.spring.backend.modules.notification.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a new notification")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationCreateDTO {
  @NotBlank(message = "Tiêu đề không được để trống")
  @Schema(description = "Title of the notification", example = "System Maintenance")
  String title;

  @NotBlank(message = "Nội dung không được để trống")
  @Schema(
      description = "Content of the notification",
      example = "The system will be down for maintenance on Saturday at 2 AM.")
  String content;

  @Schema(description = "Type of the notification", example = "INFO, ALERT, WARNING")
  NotificationType type;

  @NotNull(message = "Người nhận không được để trống")
  @Schema(description = "List of receiver user IDs", example = "[1, 2, 3]")
  List<Long> receiverIds;
}
