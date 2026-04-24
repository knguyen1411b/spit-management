package com.spring.backend.modules.task;

import com.spring.backend.exception.AppException;
import com.spring.backend.modules.member.MemberRepository;
import com.spring.backend.modules.semester.SemesterMessages;
import com.spring.backend.modules.semester.SemesterRepository;
import com.spring.backend.modules.task.dto.*;
import com.spring.backend.modules.task_request.TaskRequest;
import com.spring.backend.modules.task_request.TaskRequestRepository;
import com.spring.backend.modules.task_request.TaskRequestStatus;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {
  TaskRepository taskRepository;
  MemberRepository memberRepository;
  SemesterRepository semesterRepository;
  TaskRequestRepository taskRequestRepository;
  ModelMapper modelMapper;

  /**
   * Retrieves a paginated list of tasks assigned to the specified user within a given semester.
   *
   * <p>This method fetches tasks from the repository that are associated with the provided user ID
   * and semester ID. The results are paginated according to the provided {@link Pageable} object
   * and mapped to {@link TaskMeDTO} objects.
   *
   * @param pageable the pagination information
   * @param userId the ID of the user whose tasks are to be retrieved
   * @param semesterId the ID of the semester to filter tasks
   * @return a paginated list of {@link TaskMeDTO} objects assigned to the user
   */
  @Transactional(readOnly = true)
  public Page<TaskMeDTO> getMyTasks(Pageable pageable, Long userId, Long semesterId) {
    return taskRepository
        .findMyTasks(userId, semesterId, pageable)
        .map(
            v ->
                TaskMeDTO.builder()
                    .id(v.getTaskId())
                    .title(v.getTitle())
                    .description(v.getDescription())
                    .date(v.getDate())
                    .type(v.getType())
                    .taskMemberId(v.getTaskMemberId())
                    .status(v.getStatus())
                    .requestedAt(v.getRequestedAt())
                    .approvedAt(v.getApprovedAt())
                    .taskMemberDescription(v.getTaskMemberDescription())
                    .build());
  }

  /**
   * Retrieves a paginated list of tasks based on the provided filter criteria.
   *
   * <p>This method fetches tasks from the repository that match the specified filter criteria and
   * belong to the semester of the currently authenticated user. The results are paginated according
   * to the provided {@link Pageable} object and mapped to {@link TaskDTO} objects.
   *
   * @param pageable the pagination information
   * @param filter the filter criteria for searching tasks
   * @return a paginated list of {@link TaskDTO} objects matching the filter criteria
   */
  @Transactional(readOnly = true)
  public Page<TaskDTO> findAll(Pageable pageable, TaskFilterDTO filter, Long semesterId) {
    Specification<Task> spec = TaskSpecification.build(filter, semesterId);
    return taskRepository.findAll(spec, pageable).map(task -> modelMapper.map(task, TaskDTO.class));
  }

  /**
   * Retrieves a task by their ID.
   *
   * <p>This method fetches a task from the repository by its ID and maps it to a {@link
   * TaskDetailDTO}. If the task is not found, it returns an empty Optional.
   *
   * @param id the ID of the task to retrieve
   * @return an Optional containing the {@link TaskDetailDTO} if found, or empty if not found
   * @throws AppException if the task is not found
   */
  @Transactional(readOnly = true)
  @PreAuthorize("hasAnyAuthority('all:all',  'task:read')")
  public TaskDetailDTO findById(Long id) {
    Task task =
        taskRepository
            .findById(id)
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, TaskMessages.NOT_FOUND.getMessage()));

    TaskDetailDTO dto = new TaskDetailDTO();
    dto.setId(task.getId());
    dto.setTitle(task.getTitle());
    dto.setDescription(task.getDescription());
    dto.setType(task.getType());
    dto.setDate(task.getDate());
    dto.setCreatedAt(task.getCreatedAt());
    dto.setUpdatedAt(task.getUpdatedAt());

    dto.setMembers(
        taskRequestRepository.findAllByTaskIs(task).stream()
            .map(
                tm ->
                    TaskDetailDTO.TaskMemberDTO.builder()
                        .id(tm.getMember().getId())
                        .firstName(tm.getMember().getFirstName())
                        .lastName(tm.getMember().getLastName())
                        .email(tm.getMember().getEmail())
                        .phone(tm.getMember().getPhone())
                        .className(tm.getMember().getClassName())
                        .avatar(tm.getMember().getAvatar())
                        .status(tm.getStatus().name())
                        .description(tm.getDescription())
                        .requestedAt(tm.getRequestedAt())
                        .approvedAt(tm.getApprovedAt())
                        .build())
            .toList());

    return dto;
  }

  /**
   * Creates a new task.
   *
   * <p>This method maps the provided {@link TaskCreateDTO} to a {@link Task} entity, sets the
   * semester ID from the currently authenticated user, and saves the task to the repository. It
   * also associates members with the task based on the member IDs provided in the DTO.
   *
   * @param taskCreateDTO the DTO containing information for creating a new task
   * @throws AppException if any error occurs during task creation
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'task:create')")
  public void create(TaskCreateDTO taskCreateDTO, Long semesterId) {
    Task task = modelMapper.map(taskCreateDTO, Task.class);
    task.setSemester(
        semesterRepository
            .findById(semesterId)
            .orElseThrow(
                () ->
                    new AppException(
                        HttpStatus.NOT_FOUND, SemesterMessages.NOT_FOUND.getMessage())));
    validateMembersExist(taskCreateDTO.getMemberIds());

    taskRepository.save(task);
    taskCreateDTO.getMemberIds().forEach(memberId -> attachMember(task, memberId));
  }

  /**
   * Updates an existing task.
   *
   * <p>This method retrieves a task by its ID, updates its fields based on the provided {@link
   * TaskUpdateDTO}, and saves the updated task back to the repository. If the task is not found, it
   * throws an {@link AppException}.
   *
   * @param id the ID of the task to update
   * @param taskUpdateDTO the DTO containing updated information for the task
   * @throws AppException if the task is not found or any error occurs during the update process
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'task:update')")
  public void update(Long id, TaskUpdateDTO taskUpdateDTO) {
    Task task =
        taskRepository
            .findById(id)
            .orElseThrow(
                () -> new AppException(HttpStatus.NOT_FOUND, TaskMessages.NOT_FOUND.getMessage()));
    if (taskUpdateDTO.getTitle() != null) task.setTitle(taskUpdateDTO.getTitle());
    if (taskUpdateDTO.getDescription() != null) task.setDescription(taskUpdateDTO.getDescription());
    if (taskUpdateDTO.getDate() != null) task.setDate(taskUpdateDTO.getDate());
    if (taskUpdateDTO.getType() != null) task.setType(taskUpdateDTO.getType());
    if (taskUpdateDTO.getMemberIds() != null) {
      validateMembersExist(taskUpdateDTO.getMemberIds());
      taskRequestRepository.deleteAllByTaskIs(task);
      taskUpdateDTO.getMemberIds().forEach(memberId -> attachMember(task, memberId));
    }
  }

  /**
   * Deletes a task by its ID.
   *
   * <p>This method retrieves a task from the repository by its ID and deletes it. If the task is
   * not found, it throws an {@link AppException}.
   *
   * @param id the ID of the task to delete
   * @throws AppException if the task is not found
   */
  @Transactional
  @PreAuthorize("hasAnyAuthority('all:all', 'task:delete')")
  public void delete(Long id) {
    if (!taskRepository.existsById(id))
      throw new AppException(HttpStatus.NOT_FOUND, TaskMessages.NOT_FOUND.getMessage());
    taskRequestRepository.deleteTaskRequestByTask_Id(id);
    taskRepository.deleteById(id);
  }

  private void validateMembersExist(Iterable<Long> memberIds) {
    if (memberIds == null) {
      return;
    }
    memberIds.forEach(
        memberId ->
            memberRepository
                .findById(memberId)
                .orElseThrow(
                    () ->
                        new AppException(
                            HttpStatus.NOT_FOUND,
                            "Thành viên với id %d không tồn tại".formatted(memberId))));
  }

  private void attachMember(Task task, Long memberId) {
    memberRepository
        .findById(memberId)
        .ifPresent(
            member ->
                taskRequestRepository.save(
                    TaskRequest.builder()
                        .task(task)
                        .member(member)
                        .status(TaskRequestStatus.NONE)
                        .build()));
  }
}
