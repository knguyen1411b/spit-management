package com.spring.backend.modules.semester;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum SemesterMessages {
  NOT_FOUND("Không tìm thấy học kỳ"),
  ALREADY_EXISTS("Học kỳ đã tồn tại"),
  CODE_ALREADY_EXISTS("Mã học kỳ đã tồn tại"),
  CREATE_SUCCESS("Tạo học kỳ thành công"),
  UPDATE_SUCCESS("Cập nhật học kỳ thành công"),
  INVALID_DATE_RANGE("Khoảng thời gian không hợp lệ"),
  DELETE_SUCCESS("Xóa học kỳ thành công"),
  CANNOT_DELETE_CURRENT_SEMESTER("Không thể xóa học kỳ hiện tại"),
  CANNOT_DELETE_LAST_SEMESTER("Không thể xóa học kỳ khi chỉ còn một học kỳ trong hệ thống"),
  INDEX_SUCCESS("Lấy danh sách học kỳ thành công"),
  COPY_SUCCESS("Sao chép học kỳ thành công"),
  SHOW_SUCCESS("Lấy thông tin học kỳ thành công");
  String message;
}
