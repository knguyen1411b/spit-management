package com.spring.backend.modules.member_avatar;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.member.Member;
import com.spring.backend.modules.member.MemberMessages;
import com.spring.backend.modules.member.MemberRepository;
import java.nio.file.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarService {
  MemberRepository memberRepository;
  CloudinaryService cloudinaryService;

  /**
   * Uploads an avatar for the member with the given ID.
   *
   * @param id the ID of the member
   * @param file the avatar file to upload
   * @throws AppException if the member is not found or if an I/O error occurs
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'member:update')")
  public void upload(Long id, MultipartFile file) {
    Member member =
        memberRepository
            .findById(id)
            .orElseThrow(
                () ->
                    new AppException(HttpStatus.NOT_FOUND, MemberMessages.NOT_FOUND.getMessage()));
    try {
      String avatarUrl = cloudinaryService.uploadAvatar(file, member.getId());
      member.setAvatar(avatarUrl);
      memberRepository.save(member);
    } catch (Exception e) {
      throw new AppException(
          HttpStatus.INTERNAL_SERVER_ERROR, AvatarMessages.UPLOAD_FAILED.getMessage());
    }
  }
}
