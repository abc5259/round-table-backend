package com.roundtable.roundtable.entity.schedule;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    List<ScheduleMember> findBySchedule(Schedule schedule);
}
