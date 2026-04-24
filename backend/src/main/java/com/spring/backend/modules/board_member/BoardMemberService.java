package com.spring.backend.modules.board_member;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.board.BoardMessages;
import com.spring.backend.modules.board.BoardRepository;
import com.spring.backend.modules.board_member.dto.BoardMemberCreateDTO;
import com.spring.backend.modules.board_member.dto.BoardMemberDTO;
import com.spring.backend.modules.board_member.dto.BoardMemberUpdateDTO;
import com.spring.backend.modules.member.MemberMessages;
import com.spring.backend.modules.member.MemberRepository;
import com.spring.backend.modules.semester.SemesterMessages;
import com.spring.backend.modules.semester.SemesterRepository;
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
public class BoardMemberService {
  BoardMemberRepository boardMembersRepository;
  MemberRepository memberRepository;
  SemesterRepository semesterRepository;
  BoardRepository boardRepository;
  ModelMapper modelMapper;

  /**
   * Finds member from boards with optional keyword filtering and pagination.
   *
   * @param boardId the ID of the board
   * @param pageable the pagination information
   * @return a paginated list of BoardDTO objects
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'board_member:read')")
  public Page<BoardMemberDTO> findAll(Long boardId, Pageable pageable, Long semesterId) {
    if (!boardRepository.existsById(boardId)) {
      throw new AppException(HttpStatus.NOT_FOUND, BoardMessages.NOT_FOUND.getMessage());
    }
    return boardMembersRepository
        .findAllByBoardIdAndSemesterId(boardId, semesterId, pageable)
        .map(boardMember -> modelMapper.map(boardMember, BoardMemberDTO.class));
  }

  /**
   * Finds a member by board ID and member ID.
   *
   * @param boardId the ID of the board
   * @param memberId the ID of the member
   * @return an Optional containing the BoardMemberDTO if found, or empty if not found
   * @throws AppException if the board member is not found
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'board_member:read')")
  public BoardMemberDTO findById(Long boardId, Long memberId, Long semesterId) {
    return boardMembersRepository
        .findByBoardIdAndMemberIdAndSemesterId(boardId, memberId, semesterId)
        .map(boardMember -> modelMapper.map(boardMember, BoardMemberDTO.class))
        .orElseThrow(
            () ->
                new AppException(HttpStatus.NOT_FOUND, BoardMemberMessages.NOT_FOUND.getMessage()));
  }

  /**
   * Creates a new member in a board.
   *
   * @param boardId the ID of the board
   * @param boardMemberCreateDTO the data for creating the board member
   * @throws AppException if the member already exists in the board or if the member is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'board_member:create')")
  public void create(Long boardId, BoardMemberCreateDTO boardMemberCreateDTO, Long semesterId) {
    if (boardMembersRepository.existsByBoardIdAndMemberIdAndSemesterId(
        boardId, boardMemberCreateDTO.getMemberId(), semesterId))
      throw new AppException(
          HttpStatus.BAD_REQUEST, BoardMemberMessages.ALREADY_EXISTS.getMessage());
    boardMembersRepository.save(
        BoardMember.builder()
            .member(
                memberRepository
                    .findById(boardMemberCreateDTO.getMemberId())
                    .orElseThrow(
                        () ->
                            new AppException(
                                HttpStatus.NOT_FOUND, MemberMessages.NOT_FOUND.getMessage())))
            .semester(
                semesterRepository
                    .findById(semesterId)
                    .orElseThrow(
                        () ->
                            new AppException(
                                HttpStatus.NOT_FOUND, SemesterMessages.NOT_FOUND.getMessage())))
            .board(
                boardRepository
                    .findById(boardId)
                    .orElseThrow(
                        () ->
                            new AppException(
                                HttpStatus.NOT_FOUND, BoardMessages.NOT_FOUND.getMessage())))
            .position(boardMemberCreateDTO.getPosition())
            .descriptionBoard(boardMemberCreateDTO.getDescriptionBoard())
            .build());
  }

  /**
   * Updates an existing member in a board.
   *
   * @param boardId the ID of the board
   * @param memberId the ID of the member
   * @param boardMemberUpdateDTO the data for updating the board member
   * @throws AppException if the board member is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'board_member:update')")
  public void update(
      Long boardId, Long memberId, BoardMemberUpdateDTO boardMemberUpdateDTO, Long semesterId) {
    BoardMember boardMemberDTO =
        boardMembersRepository
            .findByBoardIdAndMemberIdAndSemesterId(boardId, memberId, semesterId)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, BoardMemberMessages.NOT_FOUND.getMessage()));
    if (boardMemberUpdateDTO.getPosition() != null) {
      boardMemberDTO.setPosition(boardMemberUpdateDTO.getPosition());
    }
    if (boardMemberUpdateDTO.getDescription() != null) {
      boardMemberDTO.setDescriptionBoard(boardMemberUpdateDTO.getDescription());
    }
  }

  /**
   * Deletes a member from a board.
   *
   * @param boardId the ID of the board
   * @param memberId the ID of the member
   * @throws AppException if the board member is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'board_member:delete')")
  public void delete(Long boardId, Long memberId, Long semesterId) {
    boolean exists =
        boardMembersRepository.existsByBoardIdAndMemberIdAndSemesterId(
            boardId, memberId, semesterId);
    if (!exists) {
      throw new AppException(HttpStatus.NOT_FOUND, BoardMemberMessages.NOT_FOUND.getMessage());
    }
    boardMembersRepository.deleteByBoardIdAndMemberIdAndSemesterId(boardId, memberId, semesterId);
  }
}
