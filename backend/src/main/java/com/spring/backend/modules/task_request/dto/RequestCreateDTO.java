package com.spring.backend.modules.task_request.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Schema(description = "Data Transfer Object for creating a task request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateDTO {
  @Schema(description = "Description of the task request", example = "Need approval for task X")
  String description;
}
