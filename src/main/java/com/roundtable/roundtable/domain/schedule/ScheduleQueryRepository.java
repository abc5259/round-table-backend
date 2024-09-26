package com.roundtable.roundtable.domain.schedule;

import static com.roundtable.roundtable.domain.member.QMember.*;
import static com.roundtable.roundtable.domain.schedule.QExtraScheduleMember.*;
import static com.roundtable.roundtable.domain.schedule.QSchedule.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleCompletion.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleDay.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleMember.*;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.schedule.dto.QScheduleDetailDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDetailDto;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ScheduleQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public ScheduleDetailDto findScheduleDetail(Long scheduleId) {
        return queryFactory
                .select(new QScheduleDetailDto(
                        schedule.id,
                        schedule.name,
                        schedule.startDate,
                        schedule.startTime,
                        schedule.divisionType,
                        schedule.category
                ))
                .from(schedule)
                .where(schedule.eq(Schedule.Id(scheduleId)))
                .fetchOne();
    }

    public List<Tuple> findSchedulesByDay(Long houseId, LocalDate now) {
        NumberExpression<Integer> sequenceCondition = new CaseBuilder()
                .when(scheduleCompletion.isNull())
                .then(schedule.sequence)
                .otherwise(schedule.sequence.subtract(1));
        StringTemplate scheduleMemberNames = Expressions.stringTemplate(
                "GROUP_CONCAT({0})", scheduleMember.member.name);
        StringTemplate extraScheduleMemberNames = Expressions.stringTemplate(
                "GROUP_CONCAT({0})", extraScheduleMember.member.name);
        return queryFactory
                .select(schedule, scheduleMemberNames.as("scheduleMemberNames"), extraScheduleMemberNames.as("extraScheduleMemberNames"))
                .from(schedule)
                .join(schedule, scheduleDay.schedule).on(scheduleDay.dayOfWeek.eq(Day.forDayOfWeek(now.getDayOfWeek())))
                .leftJoin(schedule, scheduleCompletion.schedule).on(scheduleCompletion.completionDate.eq(now))
                .join(schedule, scheduleMember.schedule).on(scheduleMember.sequence.eq(sequenceCondition))
                .join(scheduleMember.member, member)
                .join(schedule, extraScheduleMember.schedule)
                .join(extraScheduleMember.member, member)
                .where(schedule.house.id.eq(houseId))
                .groupBy(schedule.id)
                .orderBy(schedule.id.asc())
                .limit(10)
                .fetch();
    }

}
