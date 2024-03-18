package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.member.QMember.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.schedule;
import static com.roundtable.roundtable.entity.schedule.QScheduleMember.*;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
                .join(scheduleMember.member, member)
                .where(scheduleMember.schedule.eq(Schedule.Id(scheduleId)))
                .fetch();
    }

    public List<ScheduleMemberDetailDto> findScheduleMember(List<Schedule> schedules) {
        NumberExpression<Integer> daysDiff = Expressions.numberTemplate(Integer.class,
                "DATEDIFF({0}, {1})",
                Expressions.currentDate(),
                schedule.startDate);

        return queryFactory
                .select(new QScheduleMemberDetailDto(
                        scheduleMember.member.id,
                        scheduleMember.member.name,
                        scheduleMember.sequence
                ))
                .from(scheduleMember)
                .join(scheduleMember.member, member)
                .join(scheduleMember.schedule, schedule)
                .where(scheduleMember.schedule.in(schedules)
                        .and(scheduleMember.schedule.divisionType.eq(DivisionType.FIX)
                                .or(scheduleMember.schedule.divisionType.eq(DivisionType.ROTATION)
                                                .and(scheduleMember.schedule.frequency.frequencyType.eq(FrequencyType.DAILY)
                                                                .and(scheduleMember.sequence.eq(
                                                                                daysDiff
                                                                                        .divide(scheduleMember.schedule.frequency.frequencyInterval)
                                                                                        .mod(scheduleMember.schedule.sequenceSize)
                                                                                        .add(1)
                                                                        )
                                                                )
                                                        )
                                                .or(scheduleMember.schedule.frequency.frequencyType.eq(FrequencyType.WEEKLY)
                                                        .and(scheduleMember.sequence.eq(
                                                                        daysDiff
                                                                                .divide(7)
                                                                                .mod(scheduleMember.schedule.sequenceSize)
                                                                                .add(1)
                                                                )
                                                        )
                                                )
                                                .or(scheduleMember.schedule.frequency.frequencyType.eq(FrequencyType.ONCE))
                                )
                        )
                )
                .fetch();
    }

}
