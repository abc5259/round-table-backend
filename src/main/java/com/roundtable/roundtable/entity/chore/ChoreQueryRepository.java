package com.roundtable.roundtable.entity.chore;

import static com.roundtable.roundtable.entity.category.QCategory.*;
import static com.roundtable.roundtable.entity.chore.QChore.*;
import static com.roundtable.roundtable.entity.chore.QChoreMember.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.category.QCategory;
import com.roundtable.roundtable.entity.category.dto.QCategoryDetailV1Dto;
import com.roundtable.roundtable.entity.chore.dto.ChoreDetailV1Dto;
import com.roundtable.roundtable.entity.chore.dto.QChoreDetailV1Dto;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.QSchedule;
import com.roundtable.roundtable.entity.schedule.Schedule;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ChoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ChoreQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ChoreDetailV1Dto> findByIdAndDate(Long memberId, LocalDate date, Long houseId) {
        Member member = Member.builder().id(memberId).build();
        House house = House.builder().id(houseId).build();

        return queryFactory
                .select(new QChoreDetailV1Dto(
                        chore.id,
                        schedule.name,
                        chore.isCompleted,
                        chore.startDate,
                        schedule.startTime,
                        new QCategoryDetailV1Dto(
                                category.id,
                                category.name,
                                category.point
                        )
                ))
                .from(choreMember)
                .join(choreMember.chore, chore).on(choreMember.member.eq(member))
                .join(chore.schedule, schedule)
                .join(schedule.category, category)
                .where(chore.startDate.eq(date).and(schedule.house.eq(house)))
                .fetch();
    }
}
