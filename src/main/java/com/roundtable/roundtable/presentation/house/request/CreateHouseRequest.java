package com.roundtable.roundtable.presentation.house.request;

import com.roundtable.roundtable.business.house.CreateHouse;
import jakarta.validation.constraints.NotBlank;

public record CreateHouseRequest(
        @NotBlank
        String name
) {
        public CreateHouse toCreateHouse() {
                return new CreateHouse(name);
        }
}
