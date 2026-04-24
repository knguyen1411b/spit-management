package com.spring.backend.modules.notification;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.notification.dto.NotificationCreateDTO;
import com.spring.backend.modules.notification.dto.NotificationDTO;
import com.spring.backend.modules.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(
    name = "09. Notification (V1)",
    description = "API endpoints for retrieving and managing user notifications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationV1Controller {
  NotificationService notificationService;

  @Operation(
      summary = "Create a new notification",
      description = "Creates a new notification for a user.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Notification created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized access",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden access",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @PostMapping
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  public ApiResponse create(
      @RequestBody @Valid NotificationCreateDTO request,
      @AuthenticationPrincipal CustomUserDetails user) {
    notificationService.createNotification(request, user.getId());
    return ApiResponse.success(NotificationMessages.CREATED_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Retrieve paginated list of notifications",
      description = "Fetches a paginated list of user notifications.",
      parameters = {
        @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
        @Parameter(name = "size", description = "Number of items per page", example = "10"),
        @Parameter(
            name = "sort",
            description = "Sorting format: property(,asc|desc)",
            example = "name,asc")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Notifications list retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            implementation =
                                NotificationV1Controller.PagedApiResponseNotificationDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @GetMapping
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  public PagedApiResponse<NotificationDTO> myNotifications(
      @Parameter(hidden = true) Pageable pageable,
      @AuthenticationPrincipal CustomUserDetails user) {
    return PagedApiResponse.success(
        notificationService.getMyNotifications(user.getId(), pageable),
        NotificationMessages.INDEX_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Mark notification as read",
      description = "Marks a specific notification as read for the authenticated user.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Notification marked as read successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized access",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Forbidden access",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @PutMapping("/{id}/read")
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  public ApiResponse markRead(
      @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
    notificationService.markAsRead(id, user.getId());
    return ApiResponse.success(NotificationMessages.MARK_READ_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Get unread notifications count",
      description = "Retrieves the count of unread notifications for the authenticated user.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Unread notifications count retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema =
                        @Schema(
                            implementation =
                                NotificationV1Controller.DataApiResponseNotificationDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Unauthorized access",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @GetMapping("/unread-count")
  @UnauthorizedApiResponse
  public DataApiResponse<Long> unreadCount(@AuthenticationPrincipal CustomUserDetails user) {
    return DataApiResponse.success(
        notificationService.getUnreadCount(user.getId()),
        NotificationMessages.UNREAD_COUNT_SUCCESS.getMessage());
  }

  public static class PagedApiResponseNotificationDTO extends PagedApiResponse<NotificationDTO> {}

  public static class DataApiResponseNotificationDTO extends DataApiResponse<Long> {}
}
