package com.spring.backend.modules.member;

import com.spring.backend.modules.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository
    extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
  /**
   * Counts the number of members with IDs in the provided list.
   *
   * @param ids List of member IDs to check.
   * @return The count of members found with the given IDs.
   */
  long countByIdIn(List<Long> ids);

  /**
   * Finds a member by their username.
   *
   * @param username The username of the member to find.
   * @return An Optional containing the found member, or empty if not found.
   */
  Optional<Member> findByUsername(String username);

  /**
   * Checks if a member exists for the given user.
   *
   * @param user The user to check for an associated member.
   * @return true if a member exists for the user, false otherwise.
   */
  boolean existsByUser(User user);

  @Query(
      """
  SELECT sm.semester.id, COUNT(sm.member.id)
  FROM SemesterMember sm
  GROUP BY sm.semester.id
""")
  List<Object[]> countMembersGroupedBySemester();

  @Query(
      """
  SELECT COUNT(sm)
  FROM SemesterMember sm
  WHERE sm.semester.id = :semesterId
""")
  long countMembersBySemesterId(@Param("semesterId") Long semesterId);
}
