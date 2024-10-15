package com.roundtable.roundtable.domain.sse.event;

import com.roundtable.roundtable.domain.sse.event.HouseInviteSseEvent.HouseInviteSseData;

public class HouseInviteSseEvent extends JsonSseEventTemplate<HouseInviteSseData> {

    private static final String EVENT_NAME = "house_invite";

    private HouseInviteSseEvent(HouseInviteSseData eventData) {
        super(eventData);
    }

    public static HouseInviteSseEvent of(String senderName, Long invitedHouseId, String invitedHouseName) {
        return new HouseInviteSseEvent(new HouseInviteSseData(senderName, invitedHouseId, invitedHouseName));
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    public record HouseInviteSseData(
            String senderName,
            Long invitedHouseId,
            String invitedHouseName
    ) {}
}
