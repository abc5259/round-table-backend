package com.roundtable.roundtable.business.delegation.dto;

import java.time.LocalDate;

public record CreateDelegationDto(
        Long scheduleId,
        String message,
        Long senderId,
        Long receiverId,
        LocalDate delegationDate
) {
}
