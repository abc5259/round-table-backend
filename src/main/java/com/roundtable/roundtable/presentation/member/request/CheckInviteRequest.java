package com.roundtable.roundtable.presentation.member.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CheckInviteRequest(
        @NotBlank(message = "이메일은 비워져 있으면 안됩니다.") @Email(message = "이메일 형식이어야 합니다.")
        String email
) {
}
