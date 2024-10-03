package com.roundtable.roundtable.business.notification.dto.response;

import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import com.roundtable.roundtable.domain.notification.NotificationType;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DelegationNotificationResponse extends NotificationResponse {
    private Long delegationId;
    private DelegationStatus status;
    private String scheduleName;

    public DelegationNotificationResponse(
            Long id,
            NotificationType type,
            LocalDateTime createdAt,
            Long delegationId,
            DelegationStatus status,
            String scheduleName
    ) {
        super(id, type, createdAt);
        this.delegationId = delegationId;
        this.status = status;
        this.scheduleName = scheduleName;
    }
}
