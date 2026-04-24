package com.spring.backend.modules.task;

import com.spring.backend.modules.statistics.dto.StatisticsCount;
import com.spring.backend.modules.statistics.dto.StatisticsTask;
import com.spring.backend.modules.task.dto.TaskMeView;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
  @Query(
      value =
          """
    SELECT
          t.id            AS taskId,
          t.title         AS title,
          t.description   AS description,
          t.date          AS date,
          t.type          AS type,
          tm.id           AS taskMemberId,
          tm.status       AS status,
          tm.requested_at AS requestedAt,
          tm.approved_at  AS approvedAt,
          tm.description  AS taskMemberDescription
    FROM tbl_task t
    JOIN tbl_task_request tm ON tm.task_id = t.id
    JOIN tbl_member m ON m.id = tm.member_id
    JOIN tbl_user u ON u.id = m.user_id
    WHERE u.id = :userId AND t.semester_id = :semesterId
    ORDER BY t.date ASC
    """,
      countQuery =
          """
      SELECT COUNT(*)
      FROM tbl_task t
      JOIN tbl_task_request tm ON tm.task_id = t.id
      JOIN tbl_member m ON m.id = tm.member_id
      JOIN tbl_user u ON u.id = m.user_id
      WHERE u.id = :userId AND t.semester_id = :semesterId
      """,
      nativeQuery = true)
  Page<TaskMeView> findMyTasks(
      @Param("userId") Long userId, @Param("semesterId") Long semesterId, Pageable pageable);

  @Query(
      value =
          """
    SELECT COUNT(DISTINCT t.id) AS totalTasks,
           COALESCE(SUM(CASE WHEN tr.status = 'APPROVED' THEN 1 ELSE 0 END), 0) AS joinedTasks,
           COALESCE(SUM(CASE WHEN tr.status <> 'APPROVED' OR tr.status IS NULL THEN 1 ELSE 0 END), 0) AS notJoinedTasks
    FROM tbl_task t LEFT JOIN tbl_task_request tr ON tr.task_id = t.id WHERE t.semester_id = :semesterId
    """,
      nativeQuery = true)
  StatisticsTask statisticTask(@Param("semesterId") Long semesterId);

  @Query(
      value =
          """
    WITH RECURSIVE months AS (
        SELECT DATE_FORMAT(CONVERT_TZ(:fromDate, '+00:00', @@session.time_zone), '%Y-%m-01') AS month_start
        UNION ALL
        SELECT DATE_ADD(month_start, INTERVAL 1 MONTH)
        FROM months
        WHERE month_start <= DATE_FORMAT(CONVERT_TZ(:toDate, '+00:00', @@session.time_zone), '%Y-%m-01')
    )
    SELECT
        DATE_FORMAT(m.month_start, '%Y-%m') AS period,
        COUNT(DISTINCT tr.member_id) AS total
    FROM months m
    LEFT JOIN tbl_task t
      ON t.date >= m.month_start
     AND t.date < DATE_ADD(m.month_start, INTERVAL 1 MONTH)
    LEFT JOIN tbl_task_request tr
      ON tr.task_id = t.id
     AND tr.status = 'APPROVED'
    GROUP BY period
    ORDER BY period;
    """,
      nativeQuery = true)
  List<StatisticsCount> statisticTaskMembersByRange(
      @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);

  @Query(
      value =
          """
    WITH RECURSIVE months AS (
        SELECT DATE_FORMAT(CONVERT_TZ(:fromDate, '+00:00', @@session.time_zone), '%Y-%m-01') AS month_start
        UNION ALL
        SELECT DATE_ADD(month_start, INTERVAL 1 MONTH)
        FROM months
        WHERE month_start <= DATE_FORMAT(CONVERT_TZ(:toDate, '+00:00', @@session.time_zone), '%Y-%m-01')
    )
    SELECT
        DATE_FORMAT(m.month_start, '%Y-%m') AS period,
        COUNT(t.id) AS total
    FROM months m
    LEFT JOIN tbl_task t
      ON t.date >= GREATEST(
            m.month_start,
            CONVERT_TZ(:fromDate, '+00:00', @@session.time_zone)
     )
     AND t.date < LEAST(
            DATE_ADD(m.month_start, INTERVAL 1 MONTH),
            DATE_ADD(CONVERT_TZ(:toDate, '+00:00', @@session.time_zone), INTERVAL 1 DAY)
     )
    GROUP BY period
    ORDER BY period;
    """,
      nativeQuery = true)
  List<StatisticsCount> statisticTasksByRange(
      @Param("fromDate") Instant fromDate, @Param("toDate") Instant toDate);
}
