package com.roundtable.roundtable.business.notification.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String type,
        LocalDateTime createdAt,
        Long invitedHouseId,
        String invitedHouseName,
        Long choreId,
        String choreName,
        String memberNames
) {
}
