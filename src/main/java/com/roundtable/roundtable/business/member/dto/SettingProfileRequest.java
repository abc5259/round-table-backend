package com.roundtable.roundtable.business.member.dto;

import jakarta.validation.constraints.NotBlank;

public record SettingProfileRequest(
        @NotBlank
        String name,

        @NotBlank
        String gender
) {
}
