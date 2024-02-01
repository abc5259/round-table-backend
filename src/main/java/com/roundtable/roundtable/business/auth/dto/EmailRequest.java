package com.roundtable.roundtable.business.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @Email @NotBlank
        String email
) {
}
