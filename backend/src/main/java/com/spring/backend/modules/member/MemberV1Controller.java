package com.spring.backend.modules.member;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.member.dto.MemberCreateDTO;
import com.spring.backend.modules.member.dto.MemberDTO;
import com.spring.backend.modules.member.dto.MemberFilterDTO;
import com.spring.backend.modules.member.dto.MemberUpdateDTO;
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
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "06. Member (V1)", description = "API endpoints for retrieving and managing members")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MemberV1Controller {
  MemberService memberService;

  /**
   * Retrieves a paginated list of members based on filter criteria.
   *
   * @param filter the filter criteria for retrieving members
   * @param pageable the pagination information
   * @return a {@link PagedApiResponse} containing a list of {@link MemberDTO} objects
   */
  @Operation(
      summary = "Get a paginated list of members",
      description = "Retrieves a paginated list of members based on filter criteria.",
      parameters = {
        @Parameter(name = "fullName", description = "Search by full name", example = "Nguyen Dinh"),
        @Parameter(
            name = "gender",
            description = "Filter by gender (true|false)",
            example = "true"),
        @Parameter(name = "email", description = "Search by email", example = "23T108@gmail.com"),
        @Parameter(name = "phone", description = "Search by phone number", example = "0912345678"),
        @Parameter(name = "className", description = "Filter by class name", example = "KTPM K47"),
        @Parameter(name = "generation", description = "Filter by generation", example = "K17"),
        @Parameter(name = "description", description = "Search by description", example = "None"),
        @Parameter(name = "username", description = "Search by linked username", example = "23T"),
        @Parameter(name = "semesterId", description = "Search by linked semester", example = "1"),
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
            description = "Members retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = PagedApiResponseMemberDTO.class))),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping
  public PagedApiResponse<MemberDTO> index(
      @Parameter(hidden = true) MemberFilterDTO filter,
      @Parameter(hidden = true) Pageable pageable) {
    return PagedApiResponse.success(
        memberService.findAll(pageable, filter), MemberMessages.INDEX_SUCCESS.getMessage());
  }

  /**
   * Retrieves a member by their unique identifier.
   *
   * @param id the unique identifier of the member to retrieve
   * @return a {@link DataApiResponse} containing the {@link MemberDTO} if found
   */
  @Operation(
      summary = "Get member by ID",
      description = "Retrieves a member by their unique identifier.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the member")},
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Member retrieved successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = DataApiResponseMemberDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Member not found",
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
  public DataApiResponse<MemberDTO> show(@PathVariable Long id) {
    return DataApiResponse.success(
        memberService.findById(id), MemberMessages.SHOW_SUCCESS.getMessage());
  }

  /**
   * Creates a new member with the provided details.
   *
   * @param memberCreateDTO the data transfer object containing member creation information
   * @return an ApiResponse indicating the success of the member creation operation
   */
  @Operation(
      summary = "Create a new member",
      description = "Creates a new member with the provided details.",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Member creation details",
              content =
                  @io.swagger.v3.oas.annotations.media.Content(
                      mediaType = "application/json",
                      schema =
                          @io.swagger.v3.oas.annotations.media.Schema(
                              implementation = MemberCreateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Member created successfully",
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
  public ApiResponse create(@RequestBody @Valid MemberCreateDTO memberCreateDTO) {
    memberService.create(memberCreateDTO);
    return ApiResponse.created(MemberMessages.CREATE_SUCCESS.getMessage());
  }

  /**
   * Updates the member information for the specified member ID.
   *
   * @param id the ID of the member to update
   * @param memberUpdateDTO the data transfer object containing updated member information
   * @return an {@link ApiResponse} indicating the success of the update operation
   */
  @Operation(
      summary = "Update member by ID",
      description = "Updates the member information for the specified member ID.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the member")},
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Member update details",
              content =
                  @io.swagger.v3.oas.annotations.media.Content(
                      mediaType = "application/json",
                      schema =
                          @io.swagger.v3.oas.annotations.media.Schema(
                              implementation = MemberUpdateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Member updated successfully",
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
  @PutMapping("/{id}")
  public ApiResponse update(
      @PathVariable Long id, @RequestBody @Valid MemberUpdateDTO memberUpdateDTO) {
    memberService.update(id, memberUpdateDTO);
    return ApiResponse.success(MemberMessages.UPDATE_SUCCESS.getMessage());
  }

  /**
   * Deletes a member by their unique identifier.
   *
   * @param id the unique identifier of the member to delete
   * @return an {@link ApiResponse} indicating the success of the deletion operation
   */
  @Operation(
      summary = "Delete member by ID",
      description = "Deletes the member with the specified ID.",
      parameters = {@Parameter(name = "id", description = "Unique identifier of the member")},
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Member deleted successfully",
            content =
                @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema =
                        @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = ApiResponse.class))),
      })
  @DeleteMapping("/{id}")
  public ApiResponse delete(@PathVariable Long id) {
    memberService.delete(id);
    return ApiResponse.success(MemberMessages.DELETE_SUCCESS.getMessage());
  }

  public static class DataApiResponseMemberDTO extends DataApiResponse<MemberDTO> {}

  public static class PagedApiResponseMemberDTO extends PagedApiResponse<MemberDTO> {}
}
