package com.roundtable.roundtable.domain.schedule;

import static com.roundtable.roundtable.domain.schedule.QSchedule.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.schedule.dto.QScheduleDetailDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDetailDto;
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
                        schedule.startDate,
                        schedule.startTime,
                        schedule.divisionType,
                        schedule.category
                ))
                .from(schedule)
                .where(schedule.eq(Schedule.Id(scheduleId)))
                .fetchOne();
    }

}
