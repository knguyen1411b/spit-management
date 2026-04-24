package com.spring.backend.modules.permission;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.permission.dto.PermissionDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@Tag(
    name = "01. Permission (V1)",
    description = "API endpoints for retrieving and managing system permissions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionV1Controller {
  PermissionService permissionService;

  /**
   * Retrieves a paginated list of permissions with optional keyword filtering.
   *
   * @param keyword optional search keyword to filter permissions by name or title
   *     (case-insensitive)
   * @param pageable pagination and sorting information (automatically handled by Spring)
   * @return a {@link PagedApiResponse} containing the list of {@link PermissionDTO} and pagination
   *     metadata
   */
  @Operation(
      summary = "Retrieve paginated list of permissions",
      description =
          "Fetches a paginated list of system permissions with optional keyword search. "
              + "If no keyword is provided, all permissions are returned.",
      parameters = {
        @Parameter(
            name = "keyword",
            description = "Search keyword to filter permissions",
            example = "read"),
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
            description = "Permissions list retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PagedApiResponsePermissionDTO.class))),
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
  public PagedApiResponse<PermissionDTO> index(
      @RequestParam(defaultValue = "") String keyword,
      @Parameter(hidden = true) Pageable pageable) {
    return PagedApiResponse.success(
        permissionService.findAll(keyword, pageable),
        PermissionMessages.INDEX_SUCCESS.getMessage());
  }

  /**
   * Retrieves the details of a specific permission by its ID.
   *
   * @param id the ID of the permission to retrieve
   * @return a {@link DataApiResponse} containing the {@link PermissionDTO} if found
   */
  @Operation(
      summary = "Retrieve permission details by ID",
      description = "Fetches the details of a specific permission using its ID.",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the permission to retrieve",
            required = true,
            example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Permission retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataApiResponsePermissionDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Permission not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("/{id}")
  public DataApiResponse<PermissionDTO> show(@PathVariable Long id) {
    return DataApiResponse.success(
        permissionService.findById(id), PermissionMessages.SHOW_SUCCESS.getMessage());
  }

  public static class PagedApiResponsePermissionDTO extends PagedApiResponse<PermissionDTO> {}

  public static class DataApiResponsePermissionDTO extends DataApiResponse<PermissionDTO> {}
}
