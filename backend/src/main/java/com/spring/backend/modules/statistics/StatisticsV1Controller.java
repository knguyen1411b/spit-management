package com.spring.backend.modules.statistics;

import com.spring.backend.common.response.DataApiResponse;
import com.spring.backend.modules.statistics.dto.StatisticTaskDTO;
import com.spring.backend.modules.statistics.dto.StatisticsMemberDTO;
import com.spring.backend.modules.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "12. Statistics (V1)", description = "APIs for statistics")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsV1Controller {
  StatisticsService statisticsService;

  @GetMapping("/tasks")
  public DataApiResponse<StatisticTaskDTO> statisticTask(
      @AuthenticationPrincipal CustomUserDetails user) {
    return DataApiResponse.success(
        statisticsService.statisticTask(user.getSemesterId()), "Statistics retrieved successfully");
  }

  @GetMapping("/members")
  public DataApiResponse<StatisticsMemberDTO> statisticMember(
      @AuthenticationPrincipal CustomUserDetails user) {
    return DataApiResponse.success(
        statisticsService.statisticMember(user.getSemesterId()),
        "Statistics retrieved successfully");
  }
}
