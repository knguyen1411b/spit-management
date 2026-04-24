package com.spring.backend.modules.board_member;

import com.spring.backend.common.entity.BaseEntity;
import com.spring.backend.modules.board.Board;
import com.spring.backend.modules.member.Member;
import com.spring.backend.modules.semester.Semester;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tbl_board_member_semester")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardMember extends BaseEntity {
  @Column(nullable = false)
  String position;

  String descriptionBoard;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  Board board;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "semester_id")
  Semester semester;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  Member member;
}
