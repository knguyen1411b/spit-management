package com.spring.backend.modules.task_request;

import com.spring.backend.modules.task.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRequestRepository extends JpaRepository<TaskRequest, Long> {

  /**
   * Find all TaskMember entries with a specific status
   *
   * @param status the status to filter TaskMember entries
   */
  List<TaskRequest> findByStatusAndTask_Semester_Id(TaskRequestStatus status, Long semesterId);

  /**
   * Find all TaskMember entries associated with a specific Task
   *
   * @param task the Task entity whose associated TaskMember entries are to be found
   * @return a list of TaskMember entries associated with the specified Task
   */
  List<TaskRequest> findAllByTaskIs(Task task);

  /**
   * Delete all TaskMember entries associated with a specific Task
   *
   * @param task the Task entity whose associated TaskMember entries are to be deleted
   */
  void deleteAllByTaskIs(Task task);

  //  /**
  //   * Delete all TaskMember entries associated with a specific Task ID
  //   *
  //   * @param taskId the ID of the Task whose associated TaskMember entries are to be deleted
  //   */
  //  @Modifying
  //  @Query("DELETE FROM tbl_task_request tr WHERE tr.task.id = :taskId")
  //  void deleteByTaskId(@Param("taskId") Long taskId);

  void deleteTaskRequestByTask_Id(Long id);
}
