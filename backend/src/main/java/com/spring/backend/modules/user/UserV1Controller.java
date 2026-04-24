package com.spring.backend.modules.user;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "03. User (V1)", description = "API endpoints for retrieving and managing system users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserV1Controller {
  UserService userService;

  /**
   * Retrieves a paginated list of users based on the provided filter criteria.
   *
   * <p>This endpoint supports filtering by keyword, enabled status, locked status, superuser
   * status, and role name. Pagination and sorting can be controlled via the page, size, and sort
   * parameters.
   *
   * @param filter the filter criteria for user retrieval (keyword, enabled, locked, superuser,
   *     roleName)
   * @param pageable pagination and sorting information (page, size, sort)
   * @return a paginated response containing user data and a success message
   */
  @Operation(
      summary = "Get a paginated list of users",
      description = "Retrieves a paginated list of users based on filter criteria.",
      parameters = {
        @Parameter(name = "keyword", description = "Search keyword for user retrieval"),
        @Parameter(name = "enabled", description = "Filter by enabled status", example = "true"),
        @Parameter(
            name = "superuser",
            description = "Filter by superuser status",
            example = "false"),
        @Parameter(name = "roleName", description = "Filter by role name", example = "admin"),
        @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
        @Parameter(name = "size", description = "Number of items per page", example = "10"),
        @Parameter(
            name = "sort",
            description = "Sorting criteria in the format: property(,asc|desc)",
            example = "username,asc")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Users retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = PagedApiResponseUserDTO.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping
  public PagedApiResponse<UserDTO> index(
      @Parameter(hidden = true) UserFilterDTO filter, @Parameter(hidden = true) Pageable pageable) {
    return PagedApiResponse.success(
        userService.findAll(filter, pageable), UserMessages.INDEX_SUCCESS.getMessage());
  }

  /**
   * Retrieves a user by their unique identifier.
   *
   * @param id the unique identifier of the user
   * @return a {@link DataApiResponse} containing the {@link UserDTO} if found
   */
  @Operation(
      summary = "Get user by ID",
      description = "Retrieves a user by their unique identifier.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the user")},
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = DataApiResponseUserDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "User not found",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("/{id}")
  public DataApiResponse<UserDTO> show(@PathVariable Long id) {
    return DataApiResponse.success(
        userService.findById(id), UserMessages.SHOW_SUCCESS.getMessage());
  }

  /**
   * Creates a new user with the provided details.
   *
   * @param userCreateDTO the data transfer object containing user creation information; must be
   *     valid
   * @return an ApiResponse indicating the success of the user creation operation
   */
  @Operation(
      summary = "Create a new user",
      description = "Creates a new user with the provided details.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "User creation details",
              content =
                  @io.swagger.v3.oas.annotations.media.Content(
                      mediaType = "application/json",
                      schema =
                          @io.swagger.v3.oas.annotations.media.Schema(
                              implementation = UserCreateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "User created successfully",
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
  public ApiResponse create(@RequestBody @Valid UserCreateDTO userCreateDTO) {
    userService.create(userCreateDTO);
    return ApiResponse.created(UserMessages.CREATE_SUCCESS.getMessage());
  }

  /**
   * Updates the user information for the specified user ID.
   *
   * @param id the ID of the user to update
   * @param userUpdateDTO the data transfer object containing updated user information
   * @return an {@link ApiResponse} indicating the success of the update operation
   */
  @Operation(
      summary = "Update user by ID",
      description = "Updates the user information for the specified user ID.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the user")},
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "User update details",
              content =
                  @io.swagger.v3.oas.annotations.media.Content(
                      mediaType = "application/json",
                      schema =
                          @io.swagger.v3.oas.annotations.media.Schema(
                              implementation = UserUpdateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
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
  @PatchMapping("/{id}")
  public ApiResponse update(
      @PathVariable Long id, @RequestBody @Valid UserUpdateDTO userUpdateDTO) {
    userService.update(id, userUpdateDTO);
    return ApiResponse.success(UserMessages.UPDATE_SUCCESS.getMessage());
  }

  /**
   * Updates the semester information for the specified user ID.
   *
   * @param id the ID of the user to update
   * @param userUpdateSemesterDTO the data transfer object containing updated semester information
   * @return an {@link ApiResponse} indicating the success of the update operation
   */
  @Operation(
      summary = "Update user semester by ID",
      description = "Updates the semester information for the specified user ID.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the user")},
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "User update details",
              content =
                  @io.swagger.v3.oas.annotations.media.Content(
                      mediaType = "application/json",
                      schema =
                          @io.swagger.v3.oas.annotations.media.Schema(
                              implementation = UserUpdateSemesterDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User updated successfully",
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
  @PatchMapping("/{id}/semester")
  public ApiResponse updateSemester(
      @PathVariable Long id, @RequestBody @Valid UserUpdateSemesterDTO userUpdateSemesterDTO) {
    userService.updateSemester(id, userUpdateSemesterDTO.getSemesterId());
    return ApiResponse.success(UserMessages.UPDATE_SEMESTER_SUCCESS.getMessage());
  }

  /**
   * Deletes a user by their unique identifier.
   *
   * @param id the unique identifier of the user to delete
   * @return an {@link ApiResponse} indicating the success of the deletion operation
   */
  @Operation(
      summary = "Delete user by ID",
      description = "Deletes the user with the specified ID.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the user")},
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "User deleted successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @DeleteMapping("/{id}")
  public ApiResponse delete(@PathVariable Long id) {
    userService.delete(id);
    return ApiResponse.success(UserMessages.DELETE_SUCCESS.getMessage());
  }

  public static class PagedApiResponseUserDTO extends PagedApiResponse<UserDTO> {}

  public static class DataApiResponseUserDTO extends DataApiResponse<UserDTO> {}
}
