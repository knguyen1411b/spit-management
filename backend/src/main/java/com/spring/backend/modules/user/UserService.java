package com.spring.backend.modules.user;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.role.RoleRepository;
import com.spring.backend.modules.user.dto.UserCreateDTO;
import com.spring.backend.modules.user.dto.UserDTO;
import com.spring.backend.modules.user.dto.UserFilterDTO;
import com.spring.backend.modules.user.dto.UserUpdateDTO;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements UserDetailsService {

  UserRepository userRepository;
  RoleRepository roleRepository;
  ModelMapper modelMapper;
  PasswordEncoder passwordEncoder;

  /**
   * Retrieves a paginated list of users based on the provided filter criteria.
   *
   * <p>The method constructs a dynamic specification using the fields from {@link UserFilterDTO},
   * allowing filtering by keyword (matching full name or email), enabled status, locked status,
   * superuser status, and role name. The results are mapped to {@link UserDTO} objects.
   *
   * @param filter the filter criteria containing keyword, enabled, locked, superuser, and role name
   * @param pageable the pagination information
   * @return a page of {@link UserDTO} objects matching the filter criteria
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'user:read', 'notification:create')")
  public Page<UserDTO> findAll(UserFilterDTO filter, Pageable pageable) {
    Specification<User> spec = UserSpecification.build(filter);
    return userRepository.findAll(spec, pageable).map(user -> modelMapper.map(user, UserDTO.class));
  }

  /**
   * Finds a user by their unique identifier.
   *
   * <p>This method retrieves a user from the repository using the provided ID. If the user is
   * found, it is mapped to a {@link UserDTO} object and returned. If the user is not found, an
   * {@link AppException} is thrown with a 404 NOT FOUND status.
   *
   * @param id the unique identifier of the user to find
   * @return a {@link UserDTO} object representing the found user
   * @throws AppException if the user with the given ID is not found
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'user:read')")
  public UserDTO findById(Long id) {
    return userRepository
        .findById(id)
        .map(user -> modelMapper.map(user, UserDTO.class))
        .orElseThrow(
            () -> new AppException(HttpStatus.NOT_FOUND, UserMessages.NOT_FOUND.getMessage()));
  }

  /**
   * Creates a new user with the provided details.
   *
   * @param userCreateDTO the DTO containing user creation details
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'user:create')")
  public void create(UserCreateDTO userCreateDTO) {
    CustomUserDetails userDetails =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = modelMapper.map(userCreateDTO, User.class);
    user.setSemesterId(userDetails.getSemesterId());
    user.setRoles(new HashSet<>(roleRepository.findAllById(userCreateDTO.getRoleIds())));
    user.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
    userRepository.save(user);
  }

  /**
   * Updates the details of an existing user identified by the given userId.
   *
   * @param id the ID of the user to update
   * @param userUpdateDTO the DTO containing updated user information
   * @throws AppException if the user with the given ID is not found
   */
  @Transactional
  @CacheEvict(value = "userDetails", key = "#id")
  @PreAuthorize("hasAnyAuthority('all:all', 'user:update')")
  public void update(Long id, UserUpdateDTO userUpdateDTO) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, UserMessages.NOT_FOUND.getMessage()));
    if (userUpdateDTO.getPassword() != null)
      user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
    if (userUpdateDTO.getEnabled() != null) user.setEnabled(userUpdateDTO.getEnabled());
    if (userUpdateDTO.getRoleIds() != null)
      user.setRoles(new HashSet<>(roleRepository.findAllById(userUpdateDTO.getRoleIds())));
  }

  /**
   * Updates the semester ID of a user identified by the given ID.
   *
   * <p>This method retrieves the user from the repository using the provided ID. If the user is
   * found, it updates the semester ID with the provided value. If the user is not found, an {@link
   * AppException} is thrown with a 404 NOT FOUND status. The operation is transactional and evicts
   * the user details cache for the updated user.
   *
   * @param id the unique identifier of the user to update
   * @param semesterId the new semester ID to set for the user
   * @throws AppException if the user with the given ID is not found
   */
  @Transactional
  @CacheEvict(value = "userDetails", key = "#id")
  public void updateSemester(Long id, Long semesterId) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, UserMessages.NOT_FOUND.getMessage()));
    user.setSemesterId(semesterId);
  }

  /**
   * Deletes a user by their unique identifier.
   *
   * <p>This method checks if the user exists before attempting deletion. If the user does not
   * exist, an {@link AppException} is thrown with a 404 NOT FOUND status. Only users with 'all:all'
   * or 'user:delete' authority can perform this operation. The operation is transactional.
   *
   * @param id the unique identifier of the user to delete
   * @throws AppException if the user does not exist
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'user:delete')")
  public void delete(Long id) {
    if (!userRepository.existsById(id)) {
      throw new AppException(HttpStatus.NOT_FOUND, UserMessages.NOT_FOUND.getMessage());
    }
    userRepository.deleteById(id);
  }

  /**
   * Loads user-specific data by username.
   *
   * <p>This method retrieves a user from the repository using the provided username. If the user is
   * found, it constructs a {@link CustomUserDetails} object containing user information and
   * authorities derived from the user's roles and permissions. If the user is not found, a {@link
   * UsernameNotFoundException} is thrown.
   *
   * @param username the username of the user to load
   * @return a {@link CustomUserDetails} object containing user information and authorities
   * @throws UsernameNotFoundException if the user with the given username is not found
   */
  @Override
  @Transactional(readOnly = true)
  @Cacheable(
      value = "userDetails",
      key = "@userRepository.findByUsername(#username).get().id",
      condition = "@userRepository.existsByUsername(#username)")
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(UserMessages.NOT_FOUND.getMessage()));
    CustomUserDetails userDetails =
        CustomUserDetails.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .enabled(user.isEnabled())
            .superuser(user.isSuperuser())
            .passwordChanged(user.isPasswordChanged())
            .semesterId(user.getSemesterId())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    if (user.getRoles() != null && !user.isSuperuser()) {
      List<GrantedAuthority> authorities =
          user.getRoles().stream()
              .flatMap(role -> role.getPermissions().stream())
              .map(permission -> new SimpleGrantedAuthority(permission.getCode()))
              .collect(Collectors.toList());
      userDetails.setAuthorities(authorities);
    }
    return userDetails;
  }
}
