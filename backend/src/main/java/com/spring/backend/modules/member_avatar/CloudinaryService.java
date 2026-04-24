package com.spring.backend.modules.member_avatar;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.backend.common.constants.MessageConstants;
import com.spring.backend.exception.AppException;
import java.io.IOException;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
  Cloudinary cloudinary;

  @SuppressWarnings("unchecked")
  public String uploadAvatar(MultipartFile file, Long memberId) {
    try {
      Map<String, Object> uploadResult =
          (Map<String, Object>)
              cloudinary
                  .uploader()
                  .upload(
                      file.getBytes(),
                      ObjectUtils.asMap(
                          "folder",
                          "spit_management_avatars",
                          "public_id",
                          "member_id_" + memberId,
                          "overwrite",
                          true,
                          "resource_type",
                          "image"));
      return (String) uploadResult.get("secure_url");
    } catch (IOException e) {
      throw new AppException(
          HttpStatus.INTERNAL_SERVER_ERROR, MessageConstants.INTERNAL_SERVER_ERROR.getMessage());
    }
  }
}
