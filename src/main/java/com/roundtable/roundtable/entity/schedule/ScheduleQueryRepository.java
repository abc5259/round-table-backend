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
import java.sql.Date;
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
                .where(schedule.eq(Schedule.Id(scheduleId)))
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
        return getFrequencyTypeEq(ONCE).and(schedule.startDate.eq(date));
    }

    private BooleanExpression getDailyScheduleByDate(LocalDate date) {
        Date sqlDate = Date.valueOf(date);

        return getFrequencyTypeEq(DAILY)
                .and(Expressions.numberTemplate(Integer.class, "function('TIMESTAMPDIFF', DAY, {0}, {1})",
                                schedule.startDate, sqlDate)
                        .mod(schedule.frequency.frequencyInterval).eq(0));
    }

    private BooleanExpression getWeeklyScheduleBy(LocalDate date) {
        return getFrequencyTypeEq(WEEKLY)
                .and(Expressions.numberTemplate(Integer.class, "function('DAYOFWEEK', {0})", date).eq(schedule.frequency.frequencyInterval)
        );
    }

    private BooleanExpression getFrequencyTypeEq(FrequencyType frequencyType) {
        return schedule.frequency.frequencyType.eq(frequencyType);
    }
}
