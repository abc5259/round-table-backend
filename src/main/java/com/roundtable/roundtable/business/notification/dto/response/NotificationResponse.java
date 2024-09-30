package com.roundtable.roundtable.business.notification.dto.response;

import com.roundtable.roundtable.domain.notification.NotificationType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract class NotificationResponse {
    private Long id;
    private NotificationType type;
    private LocalDateTime createdAt;

    public NotificationResponse(Long id, NotificationType type, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
    }
}
