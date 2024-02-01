package com.roundtable.roundtable.business.house.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateHouseRequest(
        @NotBlank
        String name
) {
}
