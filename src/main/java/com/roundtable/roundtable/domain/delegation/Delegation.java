package com.roundtable.roundtable.domain.delegation;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Builder
    private Delegation(Long id, String message, Schedule schedule, Member sender, Member receiver, DelegationStatus status) {
        this.id = id;
        this.message = message;
        this.schedule = schedule;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }

    public static Delegation create(
            String message,
            Schedule schedule,
            Member sender,
            Member receiver
    ) {

        return Delegation.builder()
                .message(message)
                .schedule(schedule)
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
