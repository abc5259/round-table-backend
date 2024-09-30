package com.roundtable.roundtable.domain.schedule;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.roundtable.roundtable.domain.member.QMember.*;
import static com.roundtable.roundtable.domain.schedule.QExtraScheduleMember.*;
import static com.roundtable.roundtable.domain.schedule.QSchedule.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleCompletion.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleDay.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleMember.*;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.common.CursorPagination;
import com.roundtable.roundtable.domain.schedule.dto.QScheduleDto;
import com.roundtable.roundtable.domain.schedule.dto.QScheduleOfMemberDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleOfMemberDto;
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

    public List<ScheduleDto> findSchedulesByDate(Long houseId, LocalDate now, CursorPagination cursorPagination) {

        NumberExpression<Integer> sequenceCondition = new CaseBuilder()
                .when(scheduleCompletion.isNull())
                .then(schedule.sequence)
                .otherwise(scheduleCompletion.sequence);

        return queryFactory
                .select(new QScheduleDto(
                        schedule.id,
                        schedule.name,
                        schedule.category,
                        scheduleCompletion.isNotNull(),
                        schedule.startTime,
                        stringTemplate("GROUP_CONCAT(DISTINCT {0})", scheduleMember.member.name),
                        stringTemplate("GROUP_CONCAT(DISTINCT {0})", extraScheduleMember.member.name)
                ))
                .from(schedule)
                .leftJoin(scheduleCompletion).on(schedule.id.eq(scheduleCompletion.schedule.id).and(scheduleCompletion.completionDate.eq(now)))
                .join(scheduleDay).on(scheduleDay.schedule.id.eq(schedule.id).and(scheduleDay.dayOfWeek.eq(Day.forDayOfWeek(now.getDayOfWeek()))))
                .join(scheduleMember).on(scheduleMember.schedule.id.eq(schedule.id).and(scheduleMember.sequence.eq(sequenceCondition)))
                .join(scheduleMember.member)
                .leftJoin(extraScheduleMember).on(extraScheduleMember.schedule.id.eq(schedule.id))
                .leftJoin(extraScheduleMember.member)
                .where(schedule.house.id.eq(houseId).and(schedule.id.gt(cursorPagination.lastId())))
                .groupBy(schedule.id)
                .orderBy(schedule.id.asc())
                .limit(cursorPagination.limit())
                .fetch();
    }

    public List<ScheduleOfMemberDto> findSchedulesByDateAndMemberId(Long houseId, LocalDate now, Long memberId, CursorPagination cursorPagination) {

        NumberExpression<Integer> sequenceCondition = new CaseBuilder()
                .when(scheduleCompletion.isNull())
                .then(schedule.sequence)
                .otherwise(scheduleCompletion.sequence);

        return queryFactory
                .select(new QScheduleOfMemberDto(
                        schedule.id,
                        schedule.name,
                        schedule.category,
                        scheduleCompletion.isNotNull(),
                        schedule.startTime
                ))
                .from(schedule)
                .leftJoin(scheduleCompletion).on(schedule.id.eq(scheduleCompletion.schedule.id).and(scheduleCompletion.completionDate.eq(now)))
                .join(scheduleDay).on(scheduleDay.schedule.id.eq(schedule.id).and(scheduleDay.dayOfWeek.eq(Day.forDayOfWeek(now.getDayOfWeek()))))
                .join(scheduleMember).on(scheduleMember.schedule.id.eq(schedule.id).and(scheduleMember.sequence.eq(sequenceCondition)))
                .join(scheduleMember.member)
                .leftJoin(extraScheduleMember).on(extraScheduleMember.schedule.id.eq(schedule.id))
                .leftJoin(extraScheduleMember.member)
                .where(schedule.house.id.eq(houseId)
                        .and(schedule.id.gt(cursorPagination.lastId()))
                        .and(scheduleMember.member.id.eq(memberId).or(extraScheduleMember.member.id.eq(memberId)))
                )
                .groupBy(schedule.id)
                .orderBy(schedule.id.asc())
                .limit(cursorPagination.limit())
                .fetch();
    }
}
