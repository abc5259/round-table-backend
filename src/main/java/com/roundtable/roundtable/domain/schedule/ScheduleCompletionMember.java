package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class ScheduleCompletionMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ScheduleCompletion scheduleCompletion;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public ScheduleCompletionMember(ScheduleCompletion scheduleCompletion, Member member) {
        this.scheduleCompletion = scheduleCompletion;
        this.member = member;
    }
}
