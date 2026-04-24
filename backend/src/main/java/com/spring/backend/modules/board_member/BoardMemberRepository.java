package com.spring.backend.modules.board_member;

import com.spring.backend.modules.semester.Semester;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardMemberRepository extends JpaRepository<BoardMember, Long> {
  /**
   * Retrieves a paginated list of board members by board ID.
   *
   * @param boardId the ID of the board
   * @param semesterId the ID of the semester
   * @param pageable pagination and sorting information
   * @return a {@link Page} of {@link BoardMember} entities matching the criteria
   */
  Page<BoardMember> findAllByBoardIdAndSemesterId(Long boardId, Long semesterId, Pageable pageable);

  /**
   * Checks if a board member exists by board ID and member ID. * @param boardId the ID of the board
   *
   * @param memberId the ID of the member
   * @param semesterId the ID of the semester
   * @return true if a board member with the specified board ID and member ID exists, false
   *     otherwise
   */
  boolean existsByBoardIdAndMemberIdAndSemesterId(Long boardId, Long memberId, Long semesterId);

  /**
   * Finds a board member by board ID and member ID.
   *
   * @param boardId the ID of the board
   * @param memberId the ID of the member
   * @param semesterId the ID of the semester
   * @return an {@link Optional} containing the {@link BoardMember} entity if found, or empty if not
   *     found
   */
  Optional<BoardMember> findByBoardIdAndMemberIdAndSemesterId(
      Long boardId, Long memberId, Long semesterId);

  /**
   * Deletes a board member by board ID and member ID.
   *
   * @param boardId the ID of the board
   * @param memberId the ID of the member
   * @param semesterId the ID of the semester
   */
  void deleteByBoardIdAndMemberIdAndSemesterId(Long boardId, Long memberId, Long semesterId);

  /**
   * Finds all board members for a given semester.
   *
   * @param semester the semester entity
   * @return a list of {@link BoardMember} entities associated with the specified semester
   */
  List<BoardMember> findAllBySemester(Semester semester);
}
