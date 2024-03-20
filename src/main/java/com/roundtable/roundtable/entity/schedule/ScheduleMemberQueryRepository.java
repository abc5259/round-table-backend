package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.member.QMember.*;
import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.schedule;
import static com.roundtable.roundtable.entity.schedule.QScheduleMember.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
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

    public List<ScheduleMember> findMembersBySchedulesWithDivisionConditions(List<Schedule> schedules) {

        return queryFactory
                .select(scheduleMember)
                .from(scheduleMember)
                .join(scheduleMember.member, member)
                .join(scheduleMember.schedule, schedule)
                .where(schedule.in(schedules)
                        .and(
                                getScheduleDivisionTypeEq(DivisionType.FIX)
                                .or(getScheduleDivisionTypeEq(DivisionType.ROTATION).and(getRotationCondition())))
                )
                .fetch();
    }

    private BooleanExpression getScheduleDivisionTypeEq(DivisionType divisionType) {
        return scheduleMember.schedule.divisionType.eq(divisionType);
    }

    private BooleanExpression getRotationCondition() {

        BooleanExpression dailyCondition = getScheduleFrequencyTypeEq(DAILY)
                .and(getSequenceEq(calculateSequenceMatchCondition(schedule.frequency.frequencyInterval)));

        BooleanExpression weeklyCondition = getScheduleFrequencyTypeEq(WEEKLY)
                .and(getSequenceEq(calculateSequenceMatchCondition(7)));

        BooleanExpression onceCondition = getScheduleFrequencyTypeEq(ONCE);

        return dailyCondition.or(weeklyCondition).or(onceCondition);
    }

    private static BooleanExpression getScheduleFrequencyTypeEq(FrequencyType frequencyType) {
        return schedule.frequency.frequencyType.eq(frequencyType);
    }

    private BooleanExpression getSequenceEq(NumberExpression<Integer> numberExpression) {
        return scheduleMember.sequence.eq(numberExpression);
    }

    private NumberExpression<Integer> calculateSequenceMatchCondition(NumberExpression<Integer> divisor) {


        return calculateDaysDifferenceFromCurrentDate().divide(divisor)
                .mod(scheduleMember.schedule.sequenceSize)
                .add(1);
    }

    private NumberExpression<Integer> calculateSequenceMatchCondition(int divisor) {

        return calculateDaysDifferenceFromCurrentDate().divide(divisor)
                .mod(scheduleMember.schedule.sequenceSize)
                .add(1);
    }

    private NumberTemplate<Integer> calculateDaysDifferenceFromCurrentDate() {
        return Expressions.numberTemplate(Integer.class,
                "DATEDIFF({0}, {1})",
                Expressions.currentDate(),
                schedule.startDate);
    }

}
