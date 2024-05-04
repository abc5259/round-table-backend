package com.roundtable.roundtable.domain.notification;


import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(Values.INVITE)
public class InviteNotification extends Notification {

    private Long invitedHouseId;

    private String invitedHouseName;
}
