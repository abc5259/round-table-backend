package com.roundtable.roundtable.business.notification.dto.response;

import com.roundtable.roundtable.domain.notification.NotificationType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedbackNotificationResponse extends NotificationResponse {
    private Long feedbackId;
    private String scheduleName;

    public FeedbackNotificationResponse(
            Long id,
            NotificationType type,
            LocalDateTime createdAt,
            Long feedbackId,
            String scheduleName
    ) {
        super(id, type, createdAt);
        this.feedbackId = feedbackId;
        this.scheduleName = scheduleName;
    }
}
