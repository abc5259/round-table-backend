package com.roundtable.roundtable.business.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ExistEmailRequest (
        @NotBlank @Email
        String email
) {
}
