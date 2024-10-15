package com.roundtable.roundtable.domain.sse.event;

import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import com.roundtable.roundtable.domain.sse.event.DelegationSseEvent.DelegationSseData;

public class DelegationSseEvent extends JsonSseEventTemplate<DelegationSseData> {

    private static final String EVENT_NAME = "delegation";

    private DelegationSseEvent(DelegationSseData eventData) {
        super(eventData);
    }

    public static DelegationSseEvent of(String senderName, Long delegationId, DelegationStatus status, String scheduleName) {
        return new DelegationSseEvent(new DelegationSseData(senderName, delegationId, status, scheduleName));
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    public record DelegationSseData(
            String senderName,
            Long delegationId,
            DelegationStatus status,
            String scheduleName
    ) {}
}
