package com.spring.backend.modules.role;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.permission.PermissionRepository;
import com.spring.backend.modules.role.dto.RoleCreateDTO;
import com.spring.backend.modules.role.dto.RoleDTO;
import com.spring.backend.modules.role.dto.RoleDetailDTO;
import com.spring.backend.modules.role.dto.RoleUpdateDTO;
import java.util.HashSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
  RoleRepository roleRepository;
  PermissionRepository permissionRepository;
  ModelMapper modelMapper;

  /**
   * Finds all roles with optional keyword filtering and pagination.
   *
   * @param keyword the keyword to filter roles by name or title (optional)
   * @param pageable the pagination information
   * @return a paginated list of RoleDTO objects
   */
  @Transactional(readOnly = true)
  @Cacheable(
      value = "roles",
      key = "#keyword + '-' + #pageable.pageNumber + '-' + #pageable.pageSize",
      condition = "#keyword != null && !#keyword.isBlank()")
  @PreAuthorize("hasAnyAuthority('all:all',  'role:read')")
  public Page<RoleDTO> findAll(String keyword, Pageable pageable) {
    return roleRepository
        .findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContaining(
            keyword, keyword, keyword, pageable)
        .map(role -> modelMapper.map(role, RoleDTO.class));
  }

  /**
   * Finds a role by its ID.
   *
   * @param id the ID of the role to find
   * @return the RoleDetailDTO object representing the found role
   * @throws AppException if the role is not found
   */
  @Transactional(readOnly = true)
  @Cacheable(value = "role", key = "#id")
  @PreAuthorize("hasAnyAuthority('all:all',  'role:read')")
  public RoleDetailDTO findById(Long id) {
    return roleRepository
        .findById(id)
        .map(role -> modelMapper.map(role, RoleDetailDTO.class))
        .orElseThrow(
            () -> new AppException(HttpStatus.NOT_FOUND, RoleMessages.NOT_FOUND.getMessage()));
  }

  /**
   * Creates a new role with the provided details.
   *
   * @param roleCreateDTO the details of the role to create
   */
  @Transactional
  @CacheEvict(
      value = {"roles"},
      allEntries = true)
  @PreAuthorize("hasAnyAuthority('all:all', 'role:create')")
  public void create(RoleCreateDTO roleCreateDTO) {
    if (roleRepository.existsByCode(roleCreateDTO.getCode())) {
      throw new AppException(HttpStatus.BAD_REQUEST, RoleMessages.CODE_EXISTS.getMessage());
    }
    Role role = modelMapper.map(roleCreateDTO, Role.class);
    role.setPermissions(
        new HashSet<>(permissionRepository.findAllById(roleCreateDTO.getPermissionIds())));
    roleRepository.save(role);
  }

  /**
   * Updates an existing role with the provided details.
   *
   * @param id the ID of the role to update
   * @param roleUpdateDTO the new details for the role
   */
  @Transactional
  @CacheEvict(
      value = {"roles", "role"},
      key = "#id",
      allEntries = true)
  @PreAuthorize("hasAnyAuthority('all:all', 'role:update')")
  public void update(Long id, RoleUpdateDTO roleUpdateDTO) {
    Role role =
        roleRepository
            .findById(id)
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, RoleMessages.NOT_FOUND.getMessage()));
    modelMapper.map(roleUpdateDTO, role);
    role.setPermissions(
        new HashSet<>(permissionRepository.findAllById(roleUpdateDTO.getPermissionIds())));
  }

  /**
   * Deletes a role by its ID.
   *
   * @param id the ID of the role to delete
   */
  @Transactional
  @CacheEvict(
      value = {"roles", "role"},
      key = "#id",
      allEntries = true)
  @PreAuthorize("hasAnyAuthority('all:all', 'role:delete')")
  public void delete(Long id) {
    if (!roleRepository.existsById(id)) {
      throw new AppException(HttpStatus.NOT_FOUND, RoleMessages.NOT_FOUND.getMessage());
    }
    roleRepository.deleteById(id);
  }
}
