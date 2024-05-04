package com.roundtable.roundtable.business.notification.dto;

import java.util.List;

public record CreateInviteNotification(
        Long senderId,
        Long invitedHouseId,
        List<String> receiverEmails
) {

}
