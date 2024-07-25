package com.roundtable.roundtable.business.notification.dto.response;

import com.roundtable.roundtable.domain.notification.NotificationType;
import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        NotificationType type,
        LocalDateTime createdAt,
        Long invitedHouseId,
        String invitedHouseName,
        Long choreId,
        String choreName,
        String memberNames
) {
}
