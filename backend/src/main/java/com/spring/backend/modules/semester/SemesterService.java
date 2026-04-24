package com.spring.backend.modules.semester;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.board_member.BoardMember;
import com.spring.backend.modules.board_member.BoardMemberRepository;
import com.spring.backend.modules.member.Member;
import com.spring.backend.modules.member.MemberRepository;
import com.spring.backend.modules.semester.dto.SemesterCopyDTO;
import com.spring.backend.modules.semester.dto.SemesterCreateDTO;
import com.spring.backend.modules.semester.dto.SemesterDTO;
import com.spring.backend.modules.semester.dto.SemesterUpdateDTO;
import com.spring.backend.modules.user.User;
import com.spring.backend.modules.user.UserRepository;
import java.time.Instant;
import java.util.*;
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
public class SemesterService {
  SemesterRepository semesterRepository;
  SemesterMemberRepository semesterMemberRepository;
  BoardMemberRepository boardMemberRepository;
  MemberRepository memberRepository;
  UserRepository userRepository;
  ModelMapper modelMapper;

  /**
   * Retrieves a paginated list of all semesters.
   *
   * @param pageable the pagination information
   * @return a {@link Page} of {@link SemesterDTO} objects
   */
  @Transactional(readOnly = true)
  public Page<SemesterDTO> findAll(Pageable pageable, String keyword) {
    Page<Semester> semesters =
        semesterRepository
            .findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                keyword, keyword, keyword, pageable);
    return semesters.map(sem -> modelMapper.map(sem, SemesterDTO.class));
  }

  /**
   * Retrieves a semester by its ID.
   *
   * @param id the ID of the semester to retrieve
   * @return an {@link Optional} containing the {@link SemesterDTO} if found, or empty if not found
   */
  @Transactional(readOnly = true)
  public SemesterDTO findById(Long id) {
    return semesterRepository
        .findById(id)
        .map(semester -> modelMapper.map(semester, SemesterDTO.class))
        .orElseThrow(
            () -> new AppException(HttpStatus.NOT_FOUND, SemesterMessages.NOT_FOUND.getMessage()));
  }

  /**
   * Creates a new semester.
   *
   * @param semesterCreateDTO the DTO containing the details of the semester to create
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'semester:create')")
  public void create(SemesterCreateDTO semesterCreateDTO) {
    validateSemester(
        null,
        semesterCreateDTO.getCode(),
        semesterCreateDTO.getStartDate(),
        semesterCreateDTO.getEndDate());
    Semester semester = modelMapper.map(semesterCreateDTO, Semester.class);

    semesterRepository.save(semester);

    List<Member> members = memberRepository.findAllById(semesterCreateDTO.getMemberIds());

    List<SemesterMember> semesterMembers = new ArrayList<>();
    for (Member member : members) {
      semesterMembers.add(
          SemesterMember.builder()
              .id(new SemesterMemberId(semester.getId(), member.getId()))
              .semester(semester)
              .member(member)
              .build());
    }

    semesterMemberRepository.saveAll(semesterMembers);
  }

  /**
   * Copies an existing semester to create a new one.
   *
   * @param semesterCopyDTO the DTO containing the details for copying the semester
   * @throws AppException if the source semester is not found or if the new semester code already
   *     exists
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'semester:create')")
  public void copy(SemesterCopyDTO semesterCopyDTO) {
    validateSemester(
        null,
        semesterCopyDTO.getCode(),
        semesterCopyDTO.getStartDate(),
        semesterCopyDTO.getEndDate());
    Semester source =
        semesterRepository
            .findById(semesterCopyDTO.getSourceSemesterId())
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, SemesterMessages.NOT_FOUND.getMessage()));

    Semester newSemester =
        Semester.builder()
            .code(semesterCopyDTO.getCode())
            .name(semesterCopyDTO.getName())
            .description(semesterCopyDTO.getDescription())
            .startDate(semesterCopyDTO.getStartDate())
            .endDate(semesterCopyDTO.getEndDate())
            .semesterOrder(semesterCopyDTO.getSemesterOrder())
            .current(semesterCopyDTO.getCurrent())
            .build();

    semesterRepository.save(newSemester);

    if (Boolean.TRUE.equals(semesterCopyDTO.getCopyMembers())) {
      List<SemesterMember> sourceSM = semesterMemberRepository.findBySemester_Id(source.getId());

      List<SemesterMember> newSemesterMembers = new ArrayList<>();
      for (SemesterMember sm : sourceSM) {
        newSemesterMembers.add(
            SemesterMember.builder()
                .id(new SemesterMemberId(newSemester.getId(), sm.getMember().getId()))
                .semester(newSemester)
                .member(sm.getMember())
                .build());
      }

      semesterMemberRepository.saveAll(newSemesterMembers);
    }

    if (Boolean.TRUE.equals(semesterCopyDTO.getCopyBoards())) {
      List<BoardMember> sourceBoards = boardMemberRepository.findAllBySemester(source);

      List<BoardMember> newBoardMembers = new ArrayList<>();

      for (BoardMember bm : sourceBoards) {
        BoardMember newBM =
            BoardMember.builder()
                .position(bm.getPosition())
                .descriptionBoard(bm.getDescriptionBoard())
                .board(bm.getBoard())
                .member(bm.getMember())
                .semester(newSemester)
                .build();

        newBoardMembers.add(newBM);
      }

      boardMemberRepository.saveAll(newBoardMembers);
    }
  }

  /**
   * Updates an existing semester.
   *
   * @param id the ID of the semester to update
   * @param semesterUpdateDTO the DTO containing the updated details of the semester
   * @throws AppException if the semester with the given ID is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'semester:update')")
  public void update(Long id, SemesterUpdateDTO semesterUpdateDTO) {
    Semester semester =
        semesterRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, SemesterMessages.NOT_FOUND.getMessage()));
    validateSemester(
        id,
        semester.getCode(),
        semesterUpdateDTO.getStartDate() != null
            ? semesterUpdateDTO.getStartDate()
            : semester.getStartDate(),
        semesterUpdateDTO.getEndDate() != null
            ? semesterUpdateDTO.getEndDate()
            : semester.getEndDate());
    if (semesterUpdateDTO.getMemberIds() != null) {

      semester.getSemesterMembers().clear();

      List<Member> members = memberRepository.findAllById(semesterUpdateDTO.getMemberIds());

      for (Member member : members) {
        semesterMemberRepository.save(
            SemesterMember.builder()
                .id(new SemesterMemberId(id, member.getId()))
                .semester(semester)
                .member(member)
                .build());
      }
    }
    if (semesterUpdateDTO.getName() != null) semester.setName(semesterUpdateDTO.getName());
    if (semesterUpdateDTO.getStartDate() != null)
      semester.setStartDate(semesterUpdateDTO.getStartDate());
    if (semesterUpdateDTO.getEndDate() != null) semester.setEndDate(semesterUpdateDTO.getEndDate());
    if (semesterUpdateDTO.getCurrent() != null) semester.setCurrent(semesterUpdateDTO.getCurrent());
    if (semesterUpdateDTO.getDescription() != null)
      semester.setDescription(semesterUpdateDTO.getDescription());
    if (semesterUpdateDTO.getSemesterOrder() != null)
      semester.setSemesterOrder(semesterUpdateDTO.getSemesterOrder());
  }

  /**
   * Deletes a semester by its ID.
   *
   * @param id the ID of the semester to delete
   * @throws AppException if the semester with the given ID is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'semester:delete')")
  public void delete(Long id) {
    Semester semester =
        semesterRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, SemesterMessages.NOT_FOUND.getMessage()));

    List<Semester> all = semesterRepository.findAll();
    if (all.size() <= 1) {
      throw new AppException(
          HttpStatus.BAD_REQUEST, SemesterMessages.CANNOT_DELETE_LAST_SEMESTER.getMessage());
    }
    if (semester.isCurrent()) {
      throw new AppException(
          HttpStatus.BAD_REQUEST, SemesterMessages.CANNOT_DELETE_CURRENT_SEMESTER.getMessage());
    }
    Semester target = all.stream().filter(s -> !s.getId().equals(id)).findFirst().orElseThrow();
    List<User> users = userRepository.findAllBySemesterId(id);
    for (User user : users) user.setSemesterId(target.getId());

    semester.getSemesterMembers().clear();
    semesterRepository.delete(semester);
  }

  /**
   * Validates the semester details.
   *
   * @param semesterId the ID of the semester (null for new semesters)
   * @param code the code of the semester
   * @param startDate the start date of the semester
   * @param endDate the end date of the semester
   * @throws AppException if validation fails
   */
  private void validateSemester(Long semesterId, String code, Instant startDate, Instant endDate) {
    if (semesterRepository.existsByCodeAndIdNot(code, semesterId == null ? -1L : semesterId)) {
      throw new AppException(
          HttpStatus.BAD_REQUEST, SemesterMessages.CODE_ALREADY_EXISTS.getMessage());
    }
    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      throw new AppException(
          HttpStatus.BAD_REQUEST, SemesterMessages.INVALID_DATE_RANGE.getMessage());
    }
  }
}
