package com.roundtable.roundtable.domain.schedule;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleMemberRepository extends JpaRepository<ScheduleMember, Long> {
    @Query("select sm from ScheduleMember sm where sm.schedule.id = :scheduleId and sm.member.id = :memberId")
    Optional<ScheduleMember> findByScheduleIdAndMemberId(@Param("scheduleId") Long scheduleId, @Param("memberId") Long memberId);

    List<ScheduleMember> findByScheduleIdAndSequence(Long scheduleId, Integer sequence);

    @Query("""
        select sm
        from Schedule s
        join ScheduleMember sm ON s.id = sm.schedule.id and s.sequence = sm.sequence
        where s.id = :scheduleId
    """)
    List<ScheduleMember> findByScheduleManagers(@Param("scheduleId") Long scheduleId);
}
