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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExtraScheduleMember extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    private LocalDate assignedDate;

    @Builder
    private ExtraScheduleMember(Long id, Schedule schedule, Member member, LocalDate assignedDate) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.assignedDate = assignedDate;
    }

    public static ExtraScheduleMember create(Schedule schedule, Member member, LocalDate assignedDate) {
        return ExtraScheduleMember.builder()
                .schedule(schedule)
                .member(member)
                .assignedDate(assignedDate)
                .build();
    }

    public ScheduleCompletionMember toScheduleCompletionMember(ScheduleCompletion scheduleCompletion) {
        return new ScheduleCompletionMember(scheduleCompletion, member);
    }
}
