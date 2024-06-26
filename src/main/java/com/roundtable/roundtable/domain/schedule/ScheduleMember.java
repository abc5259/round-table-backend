package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class ScheduleMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer sequence;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @Builder
    private ScheduleMember(Member member, Schedule schedule, Integer sequence) {
        this.member = member;
        this.schedule = schedule;
        this.sequence = sequence;
    }

    public static ScheduleMember of(Member member, Schedule schedule, Integer sequence) {

        return ScheduleMember.builder()
                .member(member)
                .schedule(schedule)
                .sequence(sequence)
                .build();
    }

    public boolean isManager() {
        return this.schedule.isEqualSequence(sequence);
    }
}
