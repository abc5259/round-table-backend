package com.roundtable.roundtable.business.notification.dto.response;

import com.roundtable.roundtable.business.member.dto.response.MemberDetailResponse;
import com.roundtable.roundtable.domain.notification.NotificationType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public abstract class NotificationResponse {
    private Long id;
    private NotificationType type;
    private LocalDateTime createdAt;
    private Long senderId;
    private String senderName;

    public NotificationResponse(Long id, NotificationType type, LocalDateTime createdAt, Long senderId, String senderName) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
        this.senderId = senderId;
        this.senderName = senderName;
    }
}
