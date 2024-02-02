package com.roundtable.roundtable.presentation.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ExistEmailRequest (
        @NotBlank @Email
        String email
) {
}
