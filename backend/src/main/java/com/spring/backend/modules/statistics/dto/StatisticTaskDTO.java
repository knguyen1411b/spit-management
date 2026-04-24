package com.spring.backend.modules.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "DTO for task statistics")
public class StatisticTaskDTO {
  @Schema(description = "Total number of tasks")
  Long totalTasks;

  @Schema(description = "Number of joined tasks")
  Long joinedTasks;

  @Schema(description = "Number of not joined tasks")
  Long notJoinedTasks;

  @Schema(description = "Number of completed tasks")
  List<ChartPoint> taskMembersPerMonth;

  @Schema(description = "Number of tasks created per month")
  List<ChartPoint> tasksPerMonth;

  @Getter
  @Builder
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class ChartPoint {
    String label;
    Long value;
  }
}
