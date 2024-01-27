package com.roundtable.roundtable.house.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateHouseRequest(
        @NotBlank
        String name
) {
}
