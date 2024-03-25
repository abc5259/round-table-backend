package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.member.QMember.*;
import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.schedule;
import static com.roundtable.roundtable.entity.schedule.QScheduleMember.*;
import static java.util.stream.Collectors.*;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.dto.QScheduleMemberDetailDto;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleMemberDetailDto;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public Map<Schedule, List<Member>> findAllocators(List<Schedule> schedules, LocalDate now) {
        List<Tuple> results = queryFactory
                .select(schedule.id, scheduleMember.id)
                .from(scheduleMember)
                .join(scheduleMember.member, member)
                .join(scheduleMember.schedule, schedule)
                .where(schedule.in(schedules)
                        .and(
                                getScheduleDivisionTypeEq(DivisionType.FIX)
                                        .or(getScheduleDivisionTypeEq(DivisionType.ROTATION).and(getRotationCondition(
                                                now))))
                )
                .fetch();

        return results.stream()
                .collect(
                        groupingBy(
                                result -> Schedule.Id(result.get(schedule.id)),
                                mapping(result -> Member.Id(result.get(scheduleMember.id)), toList())
                        )
                );
    }

    private BooleanExpression getScheduleDivisionTypeEq(DivisionType divisionType) {
        return schedule.divisionType.eq(divisionType);
    }

    private BooleanExpression getRotationCondition(LocalDate now) {

        BooleanExpression dailyCondition = getScheduleFrequencyTypeEq(DAILY)
                .and(getSequenceEq(calculateSequenceMatchCondition(schedule.frequency.frequencyInterval,
                        now)));

        BooleanExpression weeklyCondition = getScheduleFrequencyTypeEq(WEEKLY)
                .and(getSequenceEq(calculateSequenceMatchCondition(7, now)));

        BooleanExpression onceCondition = getScheduleFrequencyTypeEq(ONCE);

        return dailyCondition.or(weeklyCondition).or(onceCondition);
    }

    private BooleanExpression getScheduleFrequencyTypeEq(FrequencyType frequencyType) {
        return schedule.frequency.frequencyType.eq(frequencyType);
    }

    private BooleanExpression getSequenceEq(NumberExpression<Integer> numberExpression) {
        return scheduleMember.sequence.eq(numberExpression);
    }

    private NumberExpression<Integer> calculateSequenceMatchCondition(NumberExpression<Integer> divisor, LocalDate now) {


        return calculateDaysDifferenceFromCurrentDate(now).divide(divisor)
                .mod(schedule.sequenceSize)
                .add(1);
    }

    private NumberExpression<Integer> calculateSequenceMatchCondition(int divisor, LocalDate now) {

        return calculateDaysDifferenceFromCurrentDate(now).divide(divisor)
                .mod(schedule.sequenceSize)
                .add(1);
    }

    private NumberTemplate<Integer> calculateDaysDifferenceFromCurrentDate(LocalDate now) {
//        Date sqlDate = Date.from();
        return Expressions.numberTemplate(Integer.class,
                "function('TIMESTAMPDIFF', DAY, {0}, {1})",
                schedule.startDate,
                now);
    }

}
