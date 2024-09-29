package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScheduleCompletion extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate completionDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @NotNull
    private Integer sequence;

    @OneToMany(mappedBy = "scheduleCompletion", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleCompletionMember> scheduleCompletionMembers = new ArrayList<>();

    @Builder
    public ScheduleCompletion(Long id, LocalDate completionDate, Schedule schedule, Integer sequence) {
        this.id = id;
        this.completionDate = completionDate;
        this.schedule = schedule;
        this.sequence = sequence;
    }

    public static ScheduleCompletion create(Schedule schedule, LocalDate completionDate, Integer sequence) {
        return ScheduleCompletion.builder()
                .schedule(schedule)
                .completionDate(completionDate)
                .sequence(sequence)
                .build();
    }

    public String getScheduleName() {
        return schedule.getName();
    }

    public void validateCreateFeedback(Member sender) {
        schedule.validateSameHouse(sender);
        validateSenderNotCompletedSchedule(sender);
    }

    private void validateSenderNotCompletedSchedule(Member sender) {
        boolean hasCompletedScheduleBySender = scheduleCompletionMembers.stream()
                .anyMatch(scheduleCompletionMember -> scheduleCompletionMember.containMemberId(sender.getId()));
        if (hasCompletedScheduleBySender) {
            throw new IllegalArgumentException("자신이 완료한 스케줄에 피드백을 남길 수 없습니다.");
        }
    }
}
