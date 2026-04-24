package com.spring.backend.modules.statistics;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.member.MemberRepository;
import com.spring.backend.modules.semester.Semester;
import com.spring.backend.modules.semester.SemesterMessages;
import com.spring.backend.modules.semester.SemesterRepository;
import com.spring.backend.modules.statistics.dto.StatisticTaskDTO;
import com.spring.backend.modules.statistics.dto.StatisticsCount;
import com.spring.backend.modules.statistics.dto.StatisticsMemberDTO;
import com.spring.backend.modules.statistics.dto.StatisticsTask;
import com.spring.backend.modules.task.TaskRepository;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsService {

  TaskRepository taskRepository;
  MemberRepository memberRepository;
  SemesterRepository semesterRepository;

  public StatisticTaskDTO statisticTask(Long semesterId) {
    StatisticsTask summary = taskRepository.statisticTask(semesterId);

    Semester semester =
        semesterRepository
            .findById(semesterId)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, SemesterMessages.NOT_FOUND.getMessage()));

    return StatisticTaskDTO.builder()
        .totalTasks(summary.getTotalTasks())
        .joinedTasks(summary.getJoinedTasks())
        .notJoinedTasks(summary.getNotJoinedTasks())
        .taskMembersPerMonth(
            mapToChart(
                taskRepository.statisticTaskMembersByRange(
                    semester.getStartDate(), semester.getEndDate())))
        .tasksPerMonth(
            mapToChart(
                taskRepository.statisticTasksByRange(
                    semester.getStartDate(), semester.getEndDate())))
        .build();
  }

  public StatisticsMemberDTO statisticMember(Long semesterId) {
    List<StatisticsMemberDTO.MemberCountBySemesterDTO> memberCountBySemesters =
        memberRepository.countMembersGroupedBySemester().stream()
            .map(
                obj ->
                    StatisticsMemberDTO.MemberCountBySemesterDTO.builder()
                        .semesterId(((Number) obj[0]).longValue())
                        .totalMembers(((Number) obj[1]).longValue())
                        .build())
            .toList();

    return StatisticsMemberDTO.builder()
        .totalMemberOfSemester(memberRepository.countMembersBySemesterId(semesterId))
        .memberCountBySemesters(memberCountBySemesters)
        .build();
  }

  private List<StatisticTaskDTO.ChartPoint> mapToChart(List<StatisticsCount> data) {
    return data.stream()
        .sorted(Comparator.comparing(StatisticsCount::getPeriod))
        .map(
            item -> {
              String[] p = item.getPeriod().split("-");
              String label = "Th" + Integer.parseInt(p[1]) + " " + p[0];
              return new StatisticTaskDTO.ChartPoint(label, item.getTotal());
            })
        .toList();
  }
}
