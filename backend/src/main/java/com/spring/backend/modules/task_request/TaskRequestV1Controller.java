package com.spring.backend.modules.task_request;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.task_request.dto.RequestCreateDTO;
import com.spring.backend.modules.task_request.dto.RequestGroupedDTO;
import com.spring.backend.modules.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task-requests")
@RequiredArgsConstructor
@Tag(name = "11. Task Request (V1)", description = "APIs for managing task requests")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskRequestV1Controller {

  TaskRequestService taskRequestRepository;

  @Operation(
      summary = "Get All Task Requests",
      description = "Retrieve all task requests grouped by their status.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tasks retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = RequestGroupedDTO.class))),
      })
  @GetMapping
  @UnauthorizedApiResponse
  public DataApiResponse<RequestGroupedDTO> getAll(
      @AuthenticationPrincipal CustomUserDetails user) {
    return DataApiResponse.success(
        taskRequestRepository.findAll(user.getSemesterId()),
        TaskRequestMessages.INDEX_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Mark Task Request as Pending",
      description = "Mark a specific task request as pending with an optional description.",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the task request to be marked as pending",
            required = true),
        @Parameter(
            name = "description",
            description = "Optional description for the pending status")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Task request marked as pending successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @PostMapping("/{id}/pending")
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @UnauthorizedApiResponse
  public ApiResponse pending(@PathVariable Long id, @RequestBody RequestCreateDTO dto) {
    taskRequestRepository.pending(id, dto.getDescription());
    return ApiResponse.success(TaskRequestMessages.PENDING_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Approve Task Request",
      description = "Approve a specific task request by its ID.",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the task request to be approved",
            required = true)
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Task request approved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @PostMapping("/{id}/approve")
  public ApiResponse approve(@PathVariable Long id) {
    taskRequestRepository.approve(id);
    return ApiResponse.success(TaskRequestMessages.APPROVE_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Reject Task Request",
      description = "Reject a specific task request by its ID.",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the task request to be rejected",
            required = true)
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Task request rejected successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @PostMapping("/{id}/reject")
  public ApiResponse reject(@PathVariable Long id) {
    taskRequestRepository.reject(id);
    return ApiResponse.success(TaskRequestMessages.REJECT_SUCCESS.getMessage());
  }
}
