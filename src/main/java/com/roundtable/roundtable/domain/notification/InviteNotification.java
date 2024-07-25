package com.roundtable.roundtable.domain.notification;


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
@DiscriminatorValue(Values.INVITE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InviteNotification extends Notification {

    private Long invitedHouseId;

    private String invitedHouseName;

    @Builder
    private InviteNotification(Member sender, Member receiver, Long invitedHouseId, String invitedHouseName) {
        super(sender, receiver, null);
        this.invitedHouseId = invitedHouseId;
        this.invitedHouseName = invitedHouseName;
    }

    public static InviteNotification create(Member sender, Member receiver, Long invitedHouseId, String invitedHouseName) {
        return InviteNotification.builder()
                .sender(sender)
                .receiver(receiver)
                .invitedHouseId(invitedHouseId)
                .invitedHouseName(invitedHouseName)
                .build();
    }
}
