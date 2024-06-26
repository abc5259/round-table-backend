package com.roundtable.roundtable.domain.schedulecomment;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleCommentErrorCode;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Embedded
    private Content content;

    @NotNull
    @ManyToOne
    private Schedule schedule;

    @NotNull
    @ManyToOne
    private Member writer;

    @Builder
    private ScheduleComment(Long id, Content content, Schedule schedule, Member writer) {
        this.id = id;
        this.content = content;
        this.schedule = schedule;
        this.writer = writer;
    }

    public static ScheduleComment create(Content content, Schedule schedule, Member writer) {
        if(!schedule.isSameHouse(writer)) {
            throw new CreateEntityException(ScheduleCommentErrorCode.NOT_SAME_HOUSE);
        }

        return ScheduleComment.builder()
                .content(content)
                .schedule(schedule)
                .writer(writer)
                .build();
    }
}
