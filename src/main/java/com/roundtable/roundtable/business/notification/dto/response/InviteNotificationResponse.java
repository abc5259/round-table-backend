package com.roundtable.roundtable.business.notification.dto.response;

import com.roundtable.roundtable.domain.notification.NotificationType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InviteNotificationResponse extends NotificationResponse {
    private Long invitedHouseId;
    private String invitedHouseName;

    public InviteNotificationResponse(
            Long id,
            NotificationType type,
            LocalDateTime createdAt,
            Long senderId,
            String senderName,
            Long invitedHouseId,
            String invitedHouseName
    ) {
        super(id, type, createdAt, senderId, senderName);
        this.invitedHouseId = invitedHouseId;
        this.invitedHouseName = invitedHouseName;
    }
}
