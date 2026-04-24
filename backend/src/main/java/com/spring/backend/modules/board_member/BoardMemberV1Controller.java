package com.spring.backend.modules.board_member;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.board_member.dto.BoardMemberCreateDTO;
import com.spring.backend.modules.board_member.dto.BoardMemberDTO;
import com.spring.backend.modules.board_member.dto.BoardMemberUpdateDTO;
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

/**
 * REST Controller for managing board members (V1).
 *
 * <p>This controller provides CRUD operations for members associated with a specific board. It
 * includes endpoints to list, retrieve, create, update, and delete board members.
 */
@RestController
@RequestMapping("/api/v1/board")
@RequiredArgsConstructor
@Tag(name = "08. Board Member (V1)", description = "Board Member API V1")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardMemberV1Controller {

  BoardMemberService boardMembersService;

  /**
   * Get paginated list of board members.
   *
   * @param boardId ID of the board
   * @param pageable pagination and sorting information
   * @return paginated response with board members
   */
  @Operation(
      summary = "Get paginated list of board members",
      description = "Retrieve a paginated list of members belonging to a specific board",
      parameters = {
        @Parameter(name = "boardId", description = "ID of the board", example = "1"),
        @Parameter(name = "page", description = "Page number (0-based index)", example = "0"),
        @Parameter(name = "size", description = "Number of items per page", example = "10"),
        @Parameter(
            name = "sort",
            description = "Sorting criteria in the format: property(,asc|desc)",
            example = "id,asc")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board members list retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PagedApiResponseBoardMemberDTO.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("{boardId}/members")
  public PagedApiResponse<BoardMemberDTO> index(
      @PathVariable Long boardId,
      @Parameter(hidden = true) Pageable pageable,
      @AuthenticationPrincipal CustomUserDetails user) {
    return PagedApiResponse.success(
        boardMembersService.findAll(boardId, pageable, user.getSemesterId()),
        BoardMemberMessages.INDEX_SUCCESS.getMessage());
  }

  /**
   * Get details of a specific board member.
   *
   * @param boardId ID of the board
   * @param memberId ID of the member
   * @return details of the board member
   */
  @Operation(
      summary = "Get a board member",
      description = "Retrieve details of a specific member belonging to a board",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board member retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataApiResponseBoardMemberDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Board member not found")
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("{boardId}/members/{memberId}")
  public DataApiResponse<BoardMemberDTO> show(
      @PathVariable Long boardId,
      @PathVariable Long memberId,
      @AuthenticationPrincipal CustomUserDetails user) {
    return DataApiResponse.success(
        boardMembersService.findById(boardId, memberId, user.getSemesterId()),
        BoardMemberMessages.SHOW_SUCCESS.getMessage());
  }

  /**
   * Create a new member in the board.
   *
   * @param boardId ID of the board
   * @param boardMemberCreateDTO request body with member details
   * @return success message
   */
  @Operation(
      summary = "Add a member to the board",
      description = "Create a new member and associate them with the board",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board member created successfully"),
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PostMapping("{boardId}/members")
  public ApiResponse create(
      @PathVariable Long boardId,
      @RequestBody @Valid BoardMemberCreateDTO boardMemberCreateDTO,
      @AuthenticationPrincipal CustomUserDetails user) {
    boardMembersService.create(boardId, boardMemberCreateDTO, user.getSemesterId());
    return ApiResponse.success(BoardMemberMessages.CREATE_SUCCESS.getMessage());
  }

  /**
   * Update an existing board member.
   *
   * @param boardId ID of the board
   * @param memberId ID of the member
   * @param boardMemberUpdateDTO request body with updated member details
   * @return success message
   */
  @Operation(
      summary = "Update a board member",
      description = "Update details of an existing member belonging to the board",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board member updated successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Board member not found")
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PutMapping("{boardId}/members/{memberId}")
  public ApiResponse update(
      @PathVariable Long boardId,
      @PathVariable Long memberId,
      @RequestBody @Valid BoardMemberUpdateDTO boardMemberUpdateDTO,
      @AuthenticationPrincipal CustomUserDetails user) {
    boardMembersService.update(boardId, memberId, boardMemberUpdateDTO, user.getSemesterId());
    return ApiResponse.success(BoardMemberMessages.UPDATE_SUCCESS.getMessage());
  }

  /**
   * Delete a board member from a board.
   *
   * @param boardId ID of the board
   * @param memberId ID of the member
   * @return success message
   */
  @Operation(
      summary = "Delete a board member",
      description = "Remove a member from the board",
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board member deleted successfully"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Board member not found")
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @DeleteMapping("{boardId}/members/{memberId}")
  public ApiResponse delete(
      @PathVariable Long boardId,
      @PathVariable Long memberId,
      @AuthenticationPrincipal CustomUserDetails user) {
    boardMembersService.delete(boardId, memberId, user.getSemesterId());
    return ApiResponse.success(BoardMemberMessages.DELETE_SUCCESS.getMessage());
  }

  public static class PagedApiResponseBoardMemberDTO extends PagedApiResponse<BoardMemberDTO> {}

  public static class DataApiResponseBoardMemberDTO extends DataApiResponse<BoardMemberDTO> {}
}
