package com.spring.backend.modules.board;

import com.spring.backend.common.response.ApiResponse;
import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.common.response.PagedApiResponse;
import com.spring.backend.common.swagger.BadRequestApiResponse;
import com.spring.backend.common.swagger.ForbiddenApiResponse;
import com.spring.backend.common.swagger.UnauthorizedApiResponse;
import com.spring.backend.modules.board.dto.BoardCreateDTO;
import com.spring.backend.modules.board.dto.BoardDTO;
import com.spring.backend.modules.board.dto.BoardUpdateDTO;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
@Tag(
    name = "07. Board (V1)",
    description = "API endpoints for retrieving and managing system boards")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardV1Controller {
  BoardService boardService;

  @Operation(
      summary = "Get paginated board list",
      description = "Retrieve a paginated list of boards with optional keyword filtering",
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
                    schema = @Schema(implementation = PagedApiResponseBoardDTO.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping
  public PagedApiResponse<BoardDTO> index(
      @RequestParam(defaultValue = "") String keyword,
      @Parameter(hidden = true) Pageable pageable) {
    return PagedApiResponse.success(
        boardService.findAll(pageable, keyword), BoardMessages.INDEX_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Get board by ID",
      description = "Retrieve a specific board by its ID",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the board to retrieve",
            required = true,
            example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board retrieved successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataApiResponseBoardDTO.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Board not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @GetMapping("/{id}")
  public DataApiResponse<BoardDTO> show(@PathVariable Long id) {
    return DataApiResponse.success(
        boardService.findById(id), BoardMessages.SHOW_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Create a new board",
      description = "Create a new board with the provided information",
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Board creation data",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = BoardCreateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Board created successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @BadRequestApiResponse
  @PostMapping
  public ApiResponse create(@RequestBody @Valid BoardCreateDTO boardCreateDTO) {
    boardService.create(boardCreateDTO);
    return ApiResponse.success(BoardMessages.CREATE_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Update an existing board",
      description = "Update the details of an existing board by its ID",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the board to update",
            required = true,
            example = "1")
      },
      requestBody =
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
              description = "Board update data",
              required = true,
              content =
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = BoardUpdateDTO.class))),
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board updated successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Board not found",
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
      @PathVariable Long id, @RequestBody @Valid BoardUpdateDTO boardUpdateDTO) {
    boardService.update(id, boardUpdateDTO);
    return ApiResponse.success(BoardMessages.UPDATE_SUCCESS.getMessage());
  }

  @Operation(
      summary = "Delete a board",
      description = "Delete an existing board by its ID",
      parameters = {
        @Parameter(
            name = "id",
            description = "ID of the board to delete",
            required = true,
            example = "1")
      },
      responses = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Board deleted successfully",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Board not found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApiResponse.class)))
      })
  @UnauthorizedApiResponse
  @ForbiddenApiResponse
  @DeleteMapping("/{id}")
  public ApiResponse delete(@PathVariable Long id) {
    boardService.delete(id);
    return ApiResponse.success(BoardMessages.DELETE_SUCCESS.getMessage());
  }

  public static class PagedApiResponseBoardDTO extends PagedApiResponse<BoardDTO> {}

  public static class DataApiResponseBoardDTO extends DataApiResponse<BoardDTO> {}
}
