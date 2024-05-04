package com.roundtable.roundtable.presentation.house.request;

import com.roundtable.roundtable.business.house.dto.CreateHouse;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateHouseRequest(
        @NotBlank(message = "하우스 이름은 필수입니다.")
        String name,

        List<String> inviteEmails
) {
        public CreateHouse toCreateHouse() {
                return new CreateHouse(name, inviteEmails);
        }
}
