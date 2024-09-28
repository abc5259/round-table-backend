package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.member.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleCompletionMemberRepository extends JpaRepository<ScheduleCompletionMember, Long> {

    @Query("""
        select scm.member
        from ScheduleCompletionMember scm
        join scm.member
        where scm.scheduleCompletion.id = :scheduleCompletionId
    """)
    List<Member> findMembersByScheduleCompletionId(Long scheduleCompletionId);
}
