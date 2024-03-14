package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.category.QCategory.*;
import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.schedule.dto.QScheduleDetailDto;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleDetailDto;
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
                        schedule.frequency,
                        schedule.startDate,
                        schedule.startTime,
                        schedule.divisionType,
                        schedule.category.id,
                        schedule.category.name,
                        schedule.category.point
                ))
                .from(schedule)
                .join(schedule.category, category)
                .where(schedule.eq(Schedule.builder().id(scheduleId).build()))
                .fetchOne();
    }

    public List<Schedule> findScheduleByDate(LocalDate date) {
        return queryFactory
                .select(schedule)
                .from(schedule)
                .where(
                        getOnceScheduleByDate(date)
                        .or(getDailyScheduleByDate(date))
                        .or(getWeeklyScheduleBy(date))
                )
                .fetch();

    }

    private BooleanExpression getOnceScheduleByDate(LocalDate date) {
        return schedule.frequency.frequencyType.eq(ONCE).and(schedule.startDate.eq(date));
    }

    private BooleanExpression getDailyScheduleByDate(LocalDate date) {
        return schedule.frequency.frequencyType.eq(DAILY)
                .and(Expressions.numberTemplate(Integer.class, "function('datediff', {0}, {1})", date,
                                schedule.startDate)
                        .mod(schedule.frequency.frequencyInterval).eq(0));
    }

    private BooleanExpression getWeeklyScheduleBy(LocalDate date) {
        return schedule.frequency.frequencyType.eq(WEEKLY).and(
                Expressions.numberTemplate(Integer.class, "WEEKDAY({0})", date)
                        .eq(schedule.frequency.frequencyInterval.subtract(1))
        );
    }
}
