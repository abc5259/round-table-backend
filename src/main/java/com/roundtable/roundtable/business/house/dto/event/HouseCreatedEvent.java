package com.roundtable.roundtable.business.house.dto.event;

import java.util.List;

public record HouseCreatedEvent(
        Long appenderId,
        Long houseId,
        List<String> invitedEmails
) {
}
