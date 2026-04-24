package com.spring.backend.modules.task_request;

import com.spring.backend.common.entity.BaseEntity;
import com.spring.backend.modules.member.Member;
import com.spring.backend.modules.task.Task;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "tbl_task_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskRequest extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "task_id", nullable = false)
  Task task;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  Member member;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  TaskRequestStatus status;

  String description;

  Instant requestedAt;

  Instant approvedAt;
}
