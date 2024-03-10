package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.category.QCategory.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.schedule.dto.QScheduleDetailDto;
import com.roundtable.roundtable.entity.schedule.dto.ScheduleDetailDto;
import jakarta.persistence.EntityManager;
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
}
