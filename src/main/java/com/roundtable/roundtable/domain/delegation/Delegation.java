package com.roundtable.roundtable.domain.delegation;

import static com.roundtable.roundtable.domain.delegation.DelegationStatus.*;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delegation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String message;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Schedule schedule;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DelegationStatus status;

    @NotNull
    private LocalDate delegationDate;

    @Builder
    public Delegation(Long id, String message, Schedule schedule, Member sender, Member receiver,
                      DelegationStatus status,
                      LocalDate delegationDate) {
        this.id = id;
        this.message = message;
        this.schedule = schedule;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.delegationDate = delegationDate;
    }


    public static Delegation create(
            List<ScheduleMember> scheduleManagers,
            String message,
            Schedule schedule,
            Member sender,
            Member receiver,
            LocalDate delegationDate
    ) {
        validateCreateDelegation(scheduleManagers, sender, receiver);

        return Delegation.builder()
                .message(message)
                .schedule(schedule)
                .sender(sender)
                .receiver(receiver)
                .delegationDate(delegationDate)
                .status(PENDING)
                .build();
    }

    private static void validateCreateDelegation(List<ScheduleMember> scheduleManagers, Member sender, Member receiver) {
        validateSender(scheduleManagers, sender);
        validateReceiver(scheduleManagers, receiver);
        validateSameHouse(sender, receiver);
    }

    private static void validateSender(List<ScheduleMember> scheduleManagers, Member sender) {
        boolean hasSender = scheduleManagers.stream().anyMatch(scheduleMember -> scheduleMember.isSameMember(sender));
        if(!hasSender) {
            throw new IllegalArgumentException("부탁은 스케줄의 오늘 담당자만 가능합니다.");
        }
    }

    private static void validateReceiver(List<ScheduleMember> scheduleManagers, Member receiver) {
        boolean hasReceiver = scheduleManagers.stream().anyMatch(scheduleMember -> scheduleMember.isSameMember(receiver));
        if(hasReceiver) {
            throw new IllegalArgumentException("오늘 스케줄 담당자에게 부탁을 할 수 없습니다.");
        }
    }

    private static void validateSameHouse(Member sender, Member receiver) {
        if(!sender.isSameHouse(receiver)) {
            throw new IllegalArgumentException("같은 하우스의 사용자에게만 부탁이 가능합니다.");
        }
    }

    public void approve(Long memberId, LocalDate now) {
        validateUpdateStatus(memberId);
        if(this.delegationDate.isAfter(now)) {
            throw new IllegalArgumentException("승인 가능한 날짜가 지났습니다.");
        }
        this.status = APPROVED;
    }

    public void reject(Long memberId, LocalDate now) {
        validateUpdateStatus(memberId);
        if(this.delegationDate.isAfter(now)) {
            throw new IllegalArgumentException("거절 가능한 날짜가 지났습니다.");
        }
        this.status = REJECTED;
    }

    private void validateUpdateStatus(Long memberId) {
        if(!receiver.getId().equals(memberId)) {
            throw new IllegalArgumentException("상태 업데이트는 부탁을 받은 사용자만 가능합니다.");
        }
    }
}
