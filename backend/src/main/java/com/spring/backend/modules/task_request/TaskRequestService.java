package com.spring.backend.modules.task_request;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.task_request.dto.RequestDTO;
import com.spring.backend.modules.task_request.dto.RequestGroupedDTO;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskRequestService {
  TaskRequestRepository taskRequestRepository;

  @Transactional
  public void pending(Long id, String description) {
    TaskRequest taskRequest = findTaskRequestOrThrow(id);
    if (taskRequest.getStatus() == TaskRequestStatus.PENDING)
      throw new AppException(
          HttpStatus.BAD_REQUEST, TaskRequestMessages.PENDING_EXISTED.getMessage());
    if (taskRequest.getStatus() == TaskRequestStatus.APPROVED)
      throw new AppException(
          HttpStatus.BAD_REQUEST, TaskRequestMessages.APPROVED_EXISTED.getMessage());

    taskRequest.setStatus(TaskRequestStatus.PENDING);
    taskRequest.setRequestedAt(Instant.now());
    taskRequest.setDescription(description);

    taskRequestRepository.save(taskRequest);
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'task_request:approve')")
  public void approve(Long id) {
    TaskRequest taskRequest = findTaskRequestOrThrow(id);
    updateStatus(taskRequest, TaskRequestStatus.APPROVED);
  }

  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'task_request:approve')")
  public void reject(Long id) {
    TaskRequest taskRequest = findTaskRequestOrThrow(id);
    updateStatus(taskRequest, TaskRequestStatus.REJECTED);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all', 'task_request:read')")
  public RequestGroupedDTO findAll(Long semesterId) {
    return RequestGroupedDTO.builder()
        .pending(
            taskRequestRepository
                .findByStatusAndTask_Semester_Id(TaskRequestStatus.PENDING, semesterId)
                .stream()
                .map(this::mapToDTO)
                .toList())
        .approved(
            taskRequestRepository
                .findByStatusAndTask_Semester_Id(TaskRequestStatus.APPROVED, semesterId)
                .stream()
                .map(this::mapToDTO)
                .toList())
        .rejected(
            taskRequestRepository
                .findByStatusAndTask_Semester_Id(TaskRequestStatus.REJECTED, semesterId)
                .stream()
                .map(this::mapToDTO)
                .toList())
        .build();
  }

  private TaskRequest findTaskRequestOrThrow(Long id) {
    return taskRequestRepository
        .findById(id)
        .orElseThrow(
            () ->
                new AppException(
                    HttpStatus.NOT_FOUND, TaskRequestMessages.TASK_REQUEST_NOT_FOUND.getMessage()));
  }

  private void updateStatus(TaskRequest taskRequest, TaskRequestStatus status) {
    taskRequest.setStatus(status);
    if (status == TaskRequestStatus.APPROVED) taskRequest.setApprovedAt(Instant.now());
    if (status == TaskRequestStatus.REJECTED) taskRequest.setApprovedAt(Instant.now());
    taskRequestRepository.save(taskRequest);
  }

  private RequestDTO mapToDTO(TaskRequest taskRequest) {
    return RequestDTO.builder()
        .id(taskRequest.getId())
        .taskId(taskRequest.getTask().getId())
        .memberId(taskRequest.getMember().getId())
        .taskTitle(taskRequest.getTask().getTitle())
        .description(taskRequest.getDescription())
        .memberName(
            taskRequest.getMember().getLastName() + " " + taskRequest.getMember().getFirstName())
        .taskDate(taskRequest.getTask().getDate())
        .status(taskRequest.getStatus())
        .requestAt(taskRequest.getRequestedAt())
        .approveAt(taskRequest.getApprovedAt())
        .description(taskRequest.getDescription())
        .createdAt(taskRequest.getCreatedAt())
        .updatedAt(taskRequest.getUpdatedAt())
        .build();
  }
}
