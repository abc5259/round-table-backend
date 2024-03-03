package com.roundtable.roundtable.entity.schedulecomment;

import com.roundtable.roundtable.entity.common.BaseEntity;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

    @Embedded
    private Content content;

    @ManyToOne
    private Schedule schedule;

    @ManyToOne
    private Member writer;

    @Builder
    private ScheduleComment(Long id, Content content, Schedule schedule, Member writer) {
        this.id = id;
        this.content = content;
        this.schedule = schedule;
        this.writer = writer;
    }
}
