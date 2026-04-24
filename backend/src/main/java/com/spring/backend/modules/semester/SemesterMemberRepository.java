package com.spring.backend.modules.semester;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterMemberRepository extends JpaRepository<SemesterMember, SemesterMemberId> {
  List<SemesterMember> findBySemester_Id(Long semesterId);
}
