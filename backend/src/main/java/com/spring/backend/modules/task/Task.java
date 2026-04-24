package com.spring.backend.modules.task;

import com.spring.backend.common.entity.BaseEntity;
import com.spring.backend.modules.semester.Semester;
import com.spring.backend.modules.task_request.TaskRequest;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity(name = "tbl_task")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task extends BaseEntity {
  @Column(nullable = false)
  String title;

  String description;

  @Column(nullable = false)
  Instant date;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  TaskType type;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "semester_id")
  Semester semester;

  @Builder.Default
  @OneToMany(mappedBy = "task", orphanRemoval = true)
  Set<TaskRequest> members = new HashSet<>();
}
