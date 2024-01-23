package com.roundtable.roundtable.member.application.dto;

import jakarta.validation.constraints.NotBlank;

public record SettingProfileRequest(
        @NotBlank
        String name,

        @NotBlank
        String gender
) {
}
