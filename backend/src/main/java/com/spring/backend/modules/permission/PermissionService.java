package com.spring.backend.modules.permission;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.permission.dto.PermissionDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
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
public class PermissionService {
  PermissionRepository permissionRepository;
  ModelMapper modelMapper;

  /**
   * Finds all permissions with optional keyword filtering.
   *
   * @param keyword the keyword to filter permissions by name or title
   * @param pageable pagination information
   * @return a page of PermissionDTO objects
   */
  @Transactional(readOnly = true)
  @Cacheable(
      value = "permissions",
      key = "#keyword + '-' + #pageable.pageNumber + '-' + #pageable.pageSize",
      condition = "#keyword != null && !#keyword.isBlank()")
  @PreAuthorize("hasAnyAuthority('all:all',  'permission:read')")
  public Page<PermissionDTO> findAll(String keyword, Pageable pageable) {
    Page<Permission> permissions =
        permissionRepository
            .findByCodeContainingIgnoreCaseOrNameContainingIgnoreCaseOrDescriptionContaining(
                keyword, keyword, keyword, pageable);
    return permissions.map(permission -> modelMapper.map(permission, PermissionDTO.class));
  }

  /**
   * Finds a permission by its ID.
   *
   * @param id the ID of the permission
   * @return the PermissionDTO object
   * @throws AppException if the permission is not found
   */
  @Transactional(readOnly = true)
  @Cacheable(value = "permission", key = "#id")
  @PreAuthorize("hasAnyAuthority('all:all',  'permission:read')")
  public PermissionDTO findById(Long id) {
    return permissionRepository
        .findById(id)
        .map(permission -> modelMapper.map(permission, PermissionDTO.class))
        .orElseThrow(
            () ->
                new AppException(HttpStatus.NOT_FOUND, PermissionMessages.NOT_FOUND.getMessage()));
  }
}
