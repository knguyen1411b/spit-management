package com.spring.backend.modules.task_request.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for check request details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestGroupedDTO {
  @Schema(description = "List of pending check requests")
  List<RequestDTO> pending;

  @Schema(description = "List of approved check requests")
  List<RequestDTO> approved;

  @Schema(description = "List of rejected check requests")
  List<RequestDTO> rejected;
}
