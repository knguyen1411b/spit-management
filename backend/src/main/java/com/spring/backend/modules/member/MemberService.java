package com.spring.backend.modules.member;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.member.dto.MemberCreateDTO;
import com.spring.backend.modules.member.dto.MemberDTO;
import com.spring.backend.modules.member.dto.MemberFilterDTO;
import com.spring.backend.modules.member.dto.MemberUpdateDTO;
import com.spring.backend.modules.user.User;
import com.spring.backend.modules.user.UserMessages;
import com.spring.backend.modules.user.UserRepository;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MemberService {
  MemberRepository memberRepository;
  UserRepository userRepository;
  ModelMapper modelMapper;

  /**
   * Retrieves a paginated list of members based on filter criteria.
   *
   * @param pageable pagination and sorting information (page, size, sort)
   * @param filter the filter criteria (name, gender, birthday, email, etc.)
   * @return a {@link Page} of {@link MemberDTO} containing matching members
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'member:read', 'task:create')")
  public Page<MemberDTO> findAll(Pageable pageable, MemberFilterDTO filter) {
    Specification<Member> spec = MemberSpecification.build(filter);

    return memberRepository
        .findAll(spec, pageable)
        .map(member -> modelMapper.map(member, MemberDTO.class));
  }

  /**
   * Finds a member by its ID.
   *
   * @param id the ID of the member
   * @return an {@link Optional} containing {@link MemberDTO} if found, otherwise empty
   * @throws AppException if the member is not found
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'member:read')")
  public MemberDTO findById(Long id) {
    return memberRepository
        .findById(id)
        .map(member -> modelMapper.map(member, MemberDTO.class))
        .orElseThrow(
            () -> new AppException(HttpStatus.NOT_FOUND, MemberMessages.NOT_FOUND.getMessage()));
  }

  /**
   * Creates a new member associated with an existing user.
   *
   * @param createDTO the DTO containing the member creation details
   * @throws AppException if the associated user is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'member:create')")
  public void create(MemberCreateDTO createDTO) {
    User user =
        userRepository
            .findByUsername(createDTO.getUsername())
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, UserMessages.NOT_FOUND.getMessage()));
    if (memberRepository.existsByUser(user)) {
      throw new AppException(
          HttpStatus.BAD_REQUEST, MemberMessages.USER_ALREADY_HAS_MEMBER.getMessage());
    }
    Member member = modelMapper.map(createDTO, Member.class);
    member.setUser(user);
    memberRepository.save(member);
  }

  /**
   * Updates an existing member.
   *
   * <p>Only non-null fields in {@link MemberUpdateDTO} will be updated. If {@code username} is
   * updated, the corresponding {@link com.spring.backend.modules.user.User} is also updated.
   *
   * @param id the ID of the member to update
   * @param updateDTO the DTO containing updated member information
   * @throws AppException if the member or associated user is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'member:update')")
  public void update(Long id, MemberUpdateDTO updateDTO) {
    Member member =
        memberRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new AppException(HttpStatus.NOT_FOUND, MemberMessages.NOT_FOUND.getMessage()));

    if (updateDTO.getLastName() != null) {
      member.setLastName(updateDTO.getLastName());
    }
    if (updateDTO.getFirstName() != null) {
      member.setFirstName(updateDTO.getFirstName());
    }
    if (updateDTO.getGender() != null) {
      member.setGender(updateDTO.getGender());
    }
    if (updateDTO.getBirthday() != null) {
      member.setBirthday(updateDTO.getBirthday());
    }
    if (updateDTO.getEmail() != null) {
      member.setEmail(updateDTO.getEmail());
    }
    if (updateDTO.getPhone() != null) {
      member.setPhone(updateDTO.getPhone());
    }
    if (updateDTO.getClassName() != null) {
      member.setClassName(updateDTO.getClassName());
    }
    if (updateDTO.getGeneration() != null) {
      member.setGeneration(updateDTO.getGeneration());
    }
    if (updateDTO.getDescription() != null) {
      member.setDescription(updateDTO.getDescription());
    }
    if (updateDTO.getUsername() != null) {
      User user =
          userRepository
              .findByUsername(updateDTO.getUsername())
              .orElseThrow(
                  () ->
                      new AppException(HttpStatus.NOT_FOUND, UserMessages.NOT_FOUND.getMessage()));
      if (!user.getId().equals(member.getUser().getId()) && memberRepository.existsByUser(user)) {
        throw new AppException(
            HttpStatus.BAD_REQUEST, MemberMessages.USER_ALREADY_HAS_MEMBER.getMessage());
      }
      member.setUsername(updateDTO.getUsername());
      member.setUser(user);
    }
  }

  /**
   * Deletes a member by ID.
   *
   * @param id the ID of the member to delete
   * @throws AppException if the member is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'member:delete')")
  public void delete(Long id) {
    Member member =
        memberRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new AppException(HttpStatus.NOT_FOUND, MemberMessages.NOT_FOUND.getMessage()));
    try {
      memberRepository.delete(member);
    } catch (DataIntegrityViolationException ex) {
      throw new AppException(HttpStatus.NOT_FOUND, MemberMessages.CANNOT_DELETE.getMessage());
    }
  }
}
