package com.spring.backend.modules.semester;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.semester.dto.SemesterCopyDTO;
import com.spring.backend.modules.semester.dto.SemesterCreateDTO;
import com.spring.backend.modules.semester.dto.SemesterDTO;
import com.spring.backend.modules.semester.dto.SemesterUpdateDTO;
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
@RequestMapping("/api/v1/semesters")
@RequiredArgsConstructor
@Tag(name = "05. Semester (V1)", description = "APIs for managing semesters")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SemesterV1Controller {
  SemesterService semesterService;

  @Operation(
      summary = "Get paginated semester list",
      description = "Retrieve a paginated list of semesters with optional keyword filtering",
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
            description = "Boards list retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PagedApiResponseSemesterDTO.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping
  public PagedApiResponse<SemesterDTO> index(
      @RequestParam(defaultValue = "") String keyword,
      @Parameter(hidden = true) Pageable pageable) {
    Page<SemesterDTO> semesters = semesterService.findAll(pageable, keyword);
    return PagedApiResponse.success(semesters, SemesterMessages.INDEX_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Get semester by ID",
      description = "Retrieve a semester by its ID",
      parameters = {
        @Parameter(name = "id", description = "ID of the semester to retrieve", example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Semester retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataApiResponseSemesterDetailDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Semester not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("/{id}")
  public DataApiResponse<SemesterDTO> show(@PathVariable Long id) {
    return DataApiResponse.success(
        semesterService.findById(id), SemesterMessages.SHOW_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Create a new semester",
      description = "Create a new semester with the provided details",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Semester creation details",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = SemesterCreateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Semester created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PostMapping
  public ApiResponse create(@RequestBody @Valid SemesterCreateDTO semesterCreateDTO) {
    semesterService.create(semesterCreateDTO);
    return ApiResponse.success(SemesterMessages.CREATE_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Copy an existing semester",
      description = "Create a new semester by copying details from an existing semester",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Semester copy details",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = SemesterCopyDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Semester created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PostMapping("/copy")
  public ApiResponse copy(@RequestBody @Valid SemesterCopyDTO semesterCopyDTO) {
    semesterService.copy(semesterCopyDTO);
    return ApiResponse.success(SemesterMessages.COPY_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Update an existing semester",
      description = "Update the details of an existing semester by its ID",
      parameters = {
        @Parameter(name = "id", description = "ID of the semester to update", example = "1")
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Semester update details",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = SemesterUpdateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Semester updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Semester not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PutMapping("/{id}")
  public ApiResponse update(
      @PathVariable Long id, @RequestBody @Valid SemesterUpdateDTO semesterUpdateDTO) {
    semesterService.update(id, semesterUpdateDTO);
    return ApiResponse.success(SemesterMessages.UPDATE_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Delete a semester",
      description = "Delete an existing semester by its ID",
      parameters = {
        @Parameter(name = "id", description = "ID of the semester to delete", example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Semester deleted successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Semester not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @DeleteMapping("/{id}")
  public ApiResponse delete(@PathVariable Long id) {
    semesterService.delete(id);
    return ApiResponse.success(SemesterMessages.DELETE_SUCCESS.getMessage());
  }

  public static class PagedApiResponseSemesterDTO extends PagedApiResponse<SemesterDTO> {}

  public static class DataApiResponseSemesterDetailDTO extends DataApiResponse<SemesterDTO> {}
}
