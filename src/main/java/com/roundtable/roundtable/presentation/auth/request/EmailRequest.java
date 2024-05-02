package com.roundtable.roundtable.presentation.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @Email(message = "이메일 형식이 아닙니다.") @NotBlank
        String email
) {
}
