package com.roundtable.roundtable.domain.chore;

import static com.roundtable.roundtable.domain.chore.QChore.*;
import static com.roundtable.roundtable.domain.chore.QChoreMember.*;
import static com.roundtable.roundtable.domain.member.QMember.*;
import static com.roundtable.roundtable.domain.schedule.QSchedule.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleDay.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.chore.dto.ChoreMembersDetailDto;
import com.roundtable.roundtable.domain.chore.dto.ChoreOfMemberDto;
import com.roundtable.roundtable.domain.chore.dto.QChoreMembersDetailDto;
import com.roundtable.roundtable.domain.chore.dto.QChoreOfMemberDto;
import com.roundtable.roundtable.domain.common.CursorPagination;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.QScheduleDay;
import com.roundtable.roundtable.domain.schedule.ScheduleDay;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
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
                        schedule.category
                ))
                .from(choreMember)
                .join(choreMember.chore, chore).on(getChoreMemberEq(memberId))
                .join(choreMember.chore.schedule, schedule)
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
                        schedule.category
                ))
                .from(choreMember)
                .join(choreMember.chore, chore)
                .join(choreMember.member, member)
                .join(chore.schedule, schedule)
                .where(getChoreStartDateEq(date).and(getScheduleHouseEq(houseId)).and(getChoreIdGt(cursor.lastId())))
                .groupBy(chore)
                .limit(cursor.limit())
                .fetch();
    }

    private BooleanExpression getChoreIdGt(Long lastChoreId) {
        return chore.id.gt(lastChoreId);
    }

    private BooleanExpression getChoreMemberEq(Long memberId) {
        return choreMember.member.eq(Member.Id(memberId));
    }

    private BooleanExpression getChoreStartDateEq(LocalDate date) {
        return chore.startDate.eq(date);
    }

    private BooleanExpression getScheduleHouseEq(Long houseId) {
        return schedule.house.eq(House.Id(houseId));
    }
}
