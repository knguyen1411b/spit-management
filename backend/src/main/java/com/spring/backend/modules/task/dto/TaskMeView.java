package com.spring.backend.modules.task.dto;

import com.spring.backend.modules.task_request.TaskRequestStatus;
import java.time.Instant;

public interface TaskMeView {
  Long getTaskId();

  String getTitle();

  String getDescription();

  Instant getDate();

  String getType();

  Long getTaskMemberId();

  TaskRequestStatus getStatus();

  Instant getRequestedAt();

  Instant getApprovedAt();

  String getTaskMemberDescription();
}
