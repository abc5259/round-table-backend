package com.roundtable.roundtable.auth.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @Email @NotBlank
        String email
) {
}
