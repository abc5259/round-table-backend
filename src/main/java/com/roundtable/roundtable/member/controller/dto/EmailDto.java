package com.roundtable.roundtable.member.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDto(
        @Email @NotBlank
        String email
) {
}
