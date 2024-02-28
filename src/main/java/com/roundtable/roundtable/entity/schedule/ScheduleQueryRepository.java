package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ScheduleQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
//
//    public void findScheduleDetail(Long scheduleId) {
//        queryFactory
//                .select(schedule)
//                .from(schedule)
//                .where(schedule.frequency.frequencyType.eq(WEEKLY))
//    }
}
