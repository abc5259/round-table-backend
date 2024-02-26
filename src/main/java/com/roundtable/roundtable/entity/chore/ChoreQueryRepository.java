package com.roundtable.roundtable.entity.chore;

import static com.roundtable.roundtable.entity.category.QCategory.*;
import static com.roundtable.roundtable.entity.chore.QChore.*;
import static com.roundtable.roundtable.entity.chore.QChoreMember.*;
import static com.roundtable.roundtable.entity.member.QMember.*;
import static com.roundtable.roundtable.entity.schedule.QSchedule.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.entity.category.dto.QCategoryDetailV1Dto;
import com.roundtable.roundtable.entity.chore.dto.ChoreMembersDetailDto;
import com.roundtable.roundtable.entity.chore.dto.ChoreOfMemberDto;
import com.roundtable.roundtable.entity.chore.dto.QChoreMembersDetailDto;
import com.roundtable.roundtable.entity.chore.dto.QChoreOfMemberDto;
import com.roundtable.roundtable.entity.common.CursorPagination;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
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

    public List<ChoreOfMemberDto> findChoresOfMember(Long memberId, LocalDate date, Long houseId) {

        return queryFactory
                .select(new QChoreOfMemberDto(
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
                .join(choreMember.chore, chore).on(getChoreMemberEq(memberId))
                .join(chore.schedule, schedule)
                .join(schedule.category, category)
                .where(getChoreStartDateEq(date).and(getScheduleHouseEq(houseId)))
                .fetch();
    }

    public List<ChoreMembersDetailDto> findChoresOfHouse(LocalDate date, Long houseId, CursorPagination cursor) {

        return queryFactory
                .select(new QChoreMembersDetailDto(
                        chore.id,
                        schedule.name,
                        chore.isCompleted,
                        chore.startDate,
                        schedule.startTime,
                        Expressions.stringTemplate("GROUP_CONCAT({0})", member.name),
                        new QCategoryDetailV1Dto(
                                category.id,
                                category.name,
                                category.point
                        )
                ))
                .from(choreMember)
                .join(choreMember.chore, chore)
                .join(choreMember.member, member)
                .join(chore.schedule, schedule)
                .join(schedule.category, category)
                .where(getChoreStartDateEq(date).and(getScheduleHouseEq(houseId)).and(getChoreIdGt(cursor.lastId())))
                .groupBy(chore)
                .limit(cursor.limit())
                .fetch();
    }

    private BooleanExpression getChoreIdGt(Long lastChoreId) {
        return chore.id.gt(lastChoreId);
    }

    private BooleanExpression getChoreMemberEq(Long memberId) {
        Member member = Member.builder().id(memberId).build();
        return choreMember.member.eq(member);
    }

    private BooleanExpression getChoreStartDateEq(LocalDate date) {
        return chore.startDate.eq(date);
    }

    private BooleanExpression getScheduleHouseEq(Long houseId) {
        House house = House.builder().id(houseId).build();
        return schedule.house.eq(house);
    }
}
