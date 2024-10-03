package com.roundtable.roundtable.presentation.delegation.request;

import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateDelegationRequest(
        @NotNull
        DelegationStatus delegationStatus
) {
}
