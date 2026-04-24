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
@Schema(description = "Data Transfer Object for updating a member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberUpdateDTO {

  @Size(max = 50, message = "Họ không được vượt quá 50 ký tự")
  @NotBlank(message = "Họ không được để trống")
  @Schema(description = "Last name of the user", example = "Nguyen")
  String lastName;

  @Size(max = 50, message = "Tên không được vượt quá 50 ký tự")
  @NotBlank(message = "Tên không được để trống")
  @Schema(description = "First name of the user", example = "Van A")
  String firstName;

  @Schema(description = "Gender of the user (true = male, false = female)", example = "true")
  Boolean gender;

  @Past(message = "Ngày sinh phải là ngày trong quá khứ")
  @Schema(description = "Birthday in format yyyy-MM-dd", example = "2003-08-20")
  Instant birthday;

  @Email(message = "Email không hợp lệ")
  @Schema(description = "Email of the user", example = "vana@example.com")
  String email;

  @Pattern(regexp = "^(0[0-9]{9})$", message = "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0")
  @Schema(description = "Phone number of the user", example = "0912345678")
  String phone;

  @Schema(description = "Class name of the user", example = "SE1701")
  String className;

  @Schema(description = "Generation of the user", example = "K17")
  String generation;

  @Schema(
      description = "Additional description or bio of the member",
      example = "Hard-working and enthusiastic student.")
  String description;

  @Schema(description = "Username of the user", example = "23T")
  String username;
}
