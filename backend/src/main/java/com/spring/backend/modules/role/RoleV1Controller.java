package com.spring.backend.modules.role;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.role.dto.RoleCreateDTO;
import com.spring.backend.modules.role.dto.RoleDTO;
import com.spring.backend.modules.role.dto.RoleDetailDTO;
import com.spring.backend.modules.role.dto.RoleUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "02. Role (V1)", description = "API endpoints for retrieving and managing system roles")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleV1Controller {
  RoleService roleService;

  /**
   * Handles HTTP GET requests to retrieve a paginated list of roles.
   *
   * @param keyword Optional search keyword to filter roles.
   * @param pageable Pagination and sorting information.
   * @return A paginated API response containing RoleDTO objects and a success message.
   */
  @Operation(
      summary = "Get paginated roles list",
      description =
          "Fetches a list of roles with optional keyword-based search and pagination. "
              + "If no keyword is provided, all roles are returned.",
      parameters = {
        @Parameter(name = "keyword", description = "Search keyword to filter", example = "read"),
        @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
        @Parameter(name = "size", description = "Number of items per page", example = "10"),
        @Parameter(
            name = "sort",
            description = "Sorting criteria in the format: property(,asc|desc)",
            example = "name,asc")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Roles list retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PagedApiResponseRoleDTO.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping
  public PagedApiResponse<RoleDTO> index(
      @RequestParam(defaultValue = "") String keyword,
      @Parameter(hidden = true) Pageable pageable) {
    Page<RoleDTO> roleDTOs = roleService.findAll(keyword, pageable);
    return PagedApiResponse.success(roleDTOs, RoleMessages.INDEX_SUCCESS.getMessage());
  }

  /**
   * Retrieves a specific role by its ID.
   *
   * @param id the ID of the role to retrieve
   * @return a DataApiResponse containing the RoleDetailDTO and a success message
   */
  @Operation(
      summary = "Get role by ID",
      description = "Fetches the details of a specific role by its ID.",
      parameters = {
        @Parameter(name = "id", description = "ID of the role to retrieve", example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Role retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataApiResponseRoleDetailDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("/{id}")
  public DataApiResponse<RoleDetailDTO> show(@PathVariable Long id) {
    return DataApiResponse.success(
        roleService.findById(id), RoleMessages.SHOW_SUCCESS.getMessage());
  }

  /**
   * Creates a new role with the provided details.
   *
   * @param roleCreateDTO the data transfer object containing role details to be created
   * @return an ApiResponse indicating the result of the creation operation
   */
  @Operation(
      summary = "Create a new role",
      description = "Creates a new role with the provided details.",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Role created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PostMapping
  public ApiResponse create(@RequestBody @Valid RoleCreateDTO roleCreateDTO) {
    roleService.create(roleCreateDTO);
    return ApiResponse.created(RoleMessages.CREATE_SUCCESS.getMessage());
  }

  /**
   * Updates an existing role with the specified ID using the provided update data.
   *
   * @param id the ID of the role to update
   * @param roleUpdateDTO the data transfer object containing updated role information
   * @return an {@link ApiResponse} indicating the success of the update operation
   */
  @Operation(
      summary = "Update role by ID",
      description =
          "Updates an existing role with the specified ID using the provided update data.",
      parameters = {
        @Parameter(name = "id", description = "ID of the role to update", example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Role updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @BadRequestApiResponse
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @PutMapping("/{id}")
  public ApiResponse update(
      @PathVariable Long id, @RequestBody @Valid RoleUpdateDTO roleUpdateDTO) {
    roleService.update(id, roleUpdateDTO);
    return ApiResponse.success(RoleMessages.UPDATE_SUCCESS.getMessage());
  }

  /**
   * Deletes a specific role by its ID.
   *
   * @param id the ID of the role to delete
   * @return an {@link ApiResponse} indicating success or failure of the deletion
   */
  @Operation(
      summary = "Delete role by ID",
      description = "Deletes a specific role by its ID.",
      parameters = {
        @Parameter(name = "id", description = "ID of the role to delete", example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Role deleted successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Role not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @DeleteMapping("/{id}")
  public ApiResponse delete(@PathVariable Long id) {
    roleService.delete(id);
    return ApiResponse.success(RoleMessages.DELETE_SUCCESS.getMessage());
  }

  public static class PagedApiResponseRoleDTO extends PagedApiResponse<RoleDTO> {}

  public static class DataApiResponseRoleDetailDTO extends DataApiResponse<RoleDetailDTO> {}
}
