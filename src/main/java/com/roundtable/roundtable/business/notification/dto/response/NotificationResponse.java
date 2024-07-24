package com.roundtable.roundtable.business.notification.dto.response;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        LocalDateTime createdAt

) {
}
