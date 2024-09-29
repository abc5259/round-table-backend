package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleCompletionMemberRepository extends JpaRepository<ScheduleCompletionMember, Long> {
    @Query("""
        select scm.member
        from ScheduleCompletionMember scm
        join scm.member
        where scm.scheduleCompletion.id = :scheduleCompletionId
    """)
    List<Member> findMembersByScheduleCompletionId(@Param("scheduleCompletionId") Long scheduleCompletionId);

    @Query("""
        select count(scm) > 0
        from ScheduleCompletionMember scm
        where scm.scheduleCompletion.id = :scheduleCompletionId and scm.member.id = :memberId
    """)
    boolean existsByScheduleCompletionIdAndMemberId(@Param("scheduleCompletionId") Long scheduleCompletionId, @Param("memberId") Long memberId);
}
