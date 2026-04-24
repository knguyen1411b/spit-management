package com.spring.backend.modules.task;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.task.dto.*;
import com.spring.backend.modules.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "10. Task (V1)", description = "API endpoints for retrieving and managing system tasks")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskV1Controller {
  TaskService taskService;

  @Operation(
      summary = "Get my tasks",
      description = "Retrieves a paginated list of tasks assigned to the authenticated user.",
      parameters = {
        @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
        @Parameter(name = "size", description = "Number of items per page", example = "10"),
        @Parameter(
            name = "sort",
            description = "Sorting criteria in the format: property(,asc|desc)",
            example = "name,asc"),
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tasks retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = PagedApiResponseTaskMeDTO.class))),
      })
  @GetMapping("/me")
  @ForbiddenApiResponse
  @UnauthorizedApiResponse
  PagedApiResponse<TaskMeDTO> getMyTasks(
      @Parameter(hidden = true) Pageable pageable,
      @AuthenticationPrincipal CustomUserDetails user) {
    return PagedApiResponse.success(
        taskService.getMyTasks(pageable, user.getId(), user.getSemesterId()),
        TaskMessages.SHOW_ME.getMessage());
  }

  /**
   * Retrieves a paginated list of tasks based on the provided filter criteria.
   *
   * @param filter the filter criteria for task retrieval
   * @param pageable pagination and sorting information (page, size, sort)
   * @return a paginated response containing task data and a success message
   */
  @Operation(
      summary = "Get a paginated list of tasks",
      description = "Retrieves a paginated list of tasks based on filter criteria.",
      parameters = {
        @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
        @Parameter(name = "size", description = "Number of items per page", example = "10"),
        @Parameter(
            name = "sort",
            description = "Sorting criteria in the format: property(,asc|desc)",
            example = "name,asc"),
        @Parameter(
            name = "keyword",
            description = "Keyword to search tasks by name or description"),
        @Parameter(name = "type", description = "Type of the task: EVENT, TASK, REMINDER, OTHER"),
        @Parameter(
            name = "fromDate",
            description = "Start date for filtering tasks (ISO 8601 format)"),
        @Parameter(name = "toDate", description = "End date for filtering tasks (ISO 8601 format)"),
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tasks retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = PagedApiResponseTaskDTO.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping
  public PagedApiResponse<TaskDTO> index(
      @Parameter(hidden = true) TaskFilterDTO filter,
      @Parameter(hidden = true) Pageable pageable,
      @AuthenticationPrincipal CustomUserDetails user) {
    return PagedApiResponse.success(
        taskService.findAll(pageable, filter, user.getSemesterId()),
        TaskMessages.INDEX_SUCCESS.getMessage());
  }

  /**
   * Retrieves a task by its unique identifier.
   *
   * @param id the unique identifier of the task
   * @return a response containing the task data and a success message
   */
  @Operation(
      summary = "Get task by ID",
      description = "Retrieves a task by their unique identifier.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the task")},
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Task retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = DataApiResponseTaskDetailDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("{id}")
  public DataApiResponse<TaskDetailDTO> show(@PathVariable Long id) {
    return DataApiResponse.success(
        taskService.findById(id), TaskMessages.SHOW_SUCCESS.getMessage());
  }

  /**
   * Creates a new task based on the provided data.
   *
   * @param taskCreateDTO the data for creating a new task
   * @return a response indicating the success of the creation operation
   */
  @Operation(
      summary = "Create a new task",
      description = "Creates a new task based on the provided data.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Task created successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PostMapping
  public ApiResponse create(
      @RequestBody @Valid TaskCreateDTO taskCreateDTO,
      @AuthenticationPrincipal CustomUserDetails user) {
    taskService.create(taskCreateDTO, user.getSemesterId());
    return ApiResponse.success(TaskMessages.CREATE_SUCCESS.getMessage());
  }

  /**
   * Updates an existing task identified by its unique identifier with the provided data.
   *
   * @param id the unique identifier of the task to be updated
   * @param taskUpdateDTO the data for updating the task
   * @return a response indicating the success of the update operation
   */
  @Operation(
      summary = "Update an existing task",
      description =
          "Updates an existing task identified by their unique identifier with the provided data.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the task")},
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Task updated successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PutMapping("{id}")
  public ApiResponse update(
      @PathVariable Long id, @RequestBody @Valid TaskUpdateDTO taskUpdateDTO) {
    taskService.update(id, taskUpdateDTO);
    return ApiResponse.success(TaskMessages.UPDATE_SUCCESS.getMessage());
  }

  /**
   * Deletes an existing task identified by its unique identifier.
   *
   * @param id the unique identifier of the task to be deleted
   * @return a response indicating the success of the deletion operation
   */
  @Operation(
      summary = "Delete an existing task",
      description = "Deletes an existing task identified by their unique identifier.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the task")},
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Task deleted successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Task not found",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @DeleteMapping("{id}")
  public ApiResponse delete(@PathVariable Long id) {
    taskService.delete(id);
    return ApiResponse.success(TaskMessages.DELETE_SUCCESS.getMessage());
  }

  public static class PagedApiResponseTaskDTO extends PagedApiResponse<TaskDTO> {}

  public static class DataApiResponseTaskDetailDTO extends DataApiResponse<TaskDetailDTO> {}

  public static class PagedApiResponseTaskMeDTO extends PagedApiResponse<TaskMeDTO> {}
}
