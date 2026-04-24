package com.spring.backend.modules.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a new member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberCreateDTO {

  @Schema(description = "Last name of the user", example = "Nguyen")
  @NotBlank(message = "Họ không được để trống")
  @Size(max = 50, message = "Họ không được vượt quá 50 ký tự")
  String lastName;

  @Schema(description = "First name of the user", example = "Van A")
  @NotBlank(message = "Tên không được để trống")
  @Size(max = 50, message = "Tên không được vượt quá 50 ký tự")
  String firstName;

  @NotNull(message = "Giới tính là bắt buộc")
  @Schema(description = "Gender of the user (true = male, false = female)", example = "true")
  Boolean gender;

  @Past(message = "Ngày sinh phải là ngày trong quá khứ")
  @Schema(description = "Birthday in format yyyy-MM-dd", example = "2003-08-15T00:00:00Z")
  Instant birthday;

  @Email(message = "Email không hợp lệ")
  @Schema(description = "Email of the user", example = "vana@example.com")
  String email;

  @Pattern(regexp = "^(0[0-9]{9})$", message = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")
  @Schema(description = "Phone number of the user", example = "0912345678")
  String phone;

  @NotBlank(message = "Tên lớp không được để trống")
  @Schema(description = "Class name of the user", example = "CNTT_K47")
  String className;

  @NotBlank(message = "Khóa học không được để trống")
  @Schema(description = "Generation of the user", example = "K1")
  String generation;

  @NotBlank(message = "Tên đăng nhập không được để trống")
  @Schema(description = "Unique username of the user", example = "23T")
  String username;

  @Schema(
      description = "Additional description or bio of the member",
      example = "Hard-working and enthusiastic student.")
  String description;
}
