package com.roundtable.roundtable.presentation.delegation.request;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.delegation.dto.CreateDelegationDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreateDelegationRequest(
        @NotNull
        Long scheduleId,
        @NotBlank
        String message,
        @NotNull
        Long receiverId
) {
    public CreateDelegationDto toCreateDelegationDto(AuthMember authMember, LocalDate now) {
        return new CreateDelegationDto(
                scheduleId,
                message,
                authMember.memberId(),
                receiverId,
                now
        );
    }
}
