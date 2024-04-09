package com.roundtable.roundtable.domain.schedulecomment;


import static com.roundtable.roundtable.domain.member.QMember.*;
import static com.roundtable.roundtable.domain.schedulecomment.QScheduleComment.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.common.CursorPagination;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedulecomment.dto.QMemberDetailDto;
import com.roundtable.roundtable.domain.schedulecomment.dto.QScheduleCommentDetailDto;
import com.roundtable.roundtable.domain.schedulecomment.dto.ScheduleCommentDetailDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCommentQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ScheduleCommentQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ScheduleCommentDetailDto> findBySchedule(Long scheduleId, CursorPagination cursorPagination) {
        return queryFactory.select(
                    new QScheduleCommentDetailDto(
                            scheduleComment.id,
                            scheduleComment.content.content,
                            new QMemberDetailDto(
                                    member.id,
                                    member.name
                            )
                    )
                )
                .from(scheduleComment)
                .join(scheduleComment.writer, member)
                .where(getEqSchedule(scheduleId).and(scheduleComment.id.gt(cursorPagination.lastId())))
                .limit(cursorPagination.limit())
                .orderBy(scheduleComment.id.asc())
                .fetch();
    }

    private BooleanExpression getEqSchedule(Long scheduleId) {
        return scheduleComment.schedule.eq(Schedule.builder().id(scheduleId).build());
    }

}
