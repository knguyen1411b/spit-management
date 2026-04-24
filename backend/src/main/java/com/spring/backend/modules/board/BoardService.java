package com.spring.backend.modules.board;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.board.dto.BoardCreateDTO;
import com.spring.backend.modules.board.dto.BoardDTO;
import com.spring.backend.modules.board.dto.BoardUpdateDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BoardService {
  BoardRepository boardRepository;
  ModelMapper modelMapper;

  /**
   * Finds all boards with optional keyword filtering and pagination.
   *
   * @param keyword the keyword to filter boards by name or title (optional)
   * @param pageable the pagination information
   * @return a paginated list of BoardDTO objects
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'board:read')")
  public Page<BoardDTO> findAll(Pageable pageable, String keyword) {
    return boardRepository
        .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword, pageable)
        .map(board -> modelMapper.map(board, BoardDTO.class));
  }

  /**
   * Finds a board by its ID.
   *
   * @param id the ID of the board
   * @return an Optional containing the BoardDTO if found, or empty if not found
   * @throws AppException if the board is not found
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'board:read')")
  public BoardDTO findById(Long id) {
    return boardRepository
        .findById(id)
        .map(board -> modelMapper.map(board, BoardDTO.class))
        .orElseThrow(
            () -> new AppException(HttpStatus.NOT_FOUND, BoardMessages.NOT_FOUND.getMessage()));
  }

  /**
   * Creates a new board.
   *
   * @param boardCreateDTO the DTO containing the information to create the board
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'board:create')")
  public void create(BoardCreateDTO boardCreateDTO) {
    Board board = modelMapper.map(boardCreateDTO, Board.class);
    boardRepository.save(board);
  }

  /**
   * Updates an existing board.
   *
   * @param id the ID of the board to update
   * @param boardUpdateDTO the DTO containing the updated information
   * @throws AppException if the board with the given ID is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'board:update')")
  public void update(Long id, BoardUpdateDTO boardUpdateDTO) {
    Board board =
        boardRepository
            .findById(id)
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, BoardMessages.NOT_FOUND.getMessage()));
    modelMapper.map(boardUpdateDTO, board);
  }

  /**
   * Deletes a board by its ID.
   *
   * @param id the ID of the board to delete
   * @throws AppException if the board with the given ID is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'board:delete')")
  public void delete(Long id) {
    Board board =
        boardRepository
            .findById(id)
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, BoardMessages.NOT_FOUND.getMessage()));
    boardRepository.delete(board);
  }
}
