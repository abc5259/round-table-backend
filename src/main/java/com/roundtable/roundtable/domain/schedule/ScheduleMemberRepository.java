package com.roundtable.roundtable.domain.schedule;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    List<ScheduleMember> findBySchedule(Schedule schedule);

    @Query("select sm from ScheduleMember sm where sm.schedule.id = :scheduleId and sm.member.id = :memberId")
    Optional<ScheduleMember> findByScheduleIdAndMemberId(@Param("scheduleId") Long scheduleId, @Param("memberId") Long memberId);

    List<ScheduleMember> findByScheduleIdAndSequence(Long scheduleId, Integer sequence);
}
