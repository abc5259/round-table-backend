package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Values.DELEGATION)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DelegationNotification extends Notification {
    private Long delegationId;
    private DelegationStatus status;
    private String scheduleName;

    @Builder
    private DelegationNotification(
            Member sender,
            Member receiver,
            House house,
            Long delegationId,
            DelegationStatus status,
            String scheduleName
    ) {
        super(sender, receiver, house);
        this.delegationId = delegationId;
        this.status = status;
        this.scheduleName = scheduleName;
    }

    public static DelegationNotification create(
            House house,
            Member sender,
            Member receiver,
            Delegation delegation,
            String scheduleName
    ) {
        return DelegationNotification.builder()
                .sender(sender)
                .receiver(receiver)
                .house(house)
                .delegationId(delegation.getId())
                .status(delegation.getStatus())
                .scheduleName(scheduleName)
                .build();
    }
}
