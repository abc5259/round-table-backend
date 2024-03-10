package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.member.QMember.*;
import static com.roundtable.roundtable.entity.schedule.QScheduleMember.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.schedule.dto.QScheduleMemberDetailDto;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleMemberDetailDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleMemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ScheduleMemberQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ScheduleMemberDetailDto> findScheduleMemberDetail(Long scheduleId) {
        return queryFactory
                .select(new QScheduleMemberDetailDto(
                        scheduleMember.member.id,
                        scheduleMember.member.name,
                        scheduleMember.sequence
                ))
                .from(scheduleMember)
                .join(scheduleMember.member, member).fetchJoin()
                .where(scheduleMember.schedule.eq(Schedule.Id(scheduleId)))
                .fetch();
    }
}
