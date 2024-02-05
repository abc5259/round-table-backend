package com.roundtable.roundtable.presentation.housework.request;

import com.roundtable.roundtable.entity.housework.CreateOneTimeHouseWork;
import com.roundtable.roundtable.entity.housework.HouseWorkCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record CreateOneTimeHouseWorkRequest (
        @NotBlank
        String name,
        @NotNull
        HouseWorkCategory houseWorkCategory,
        @NotNull
        LocalDateTime assignedDate,
        @NotEmpty
        List<Long> assignedMembersId
        ) {
        public CreateOneTimeHouseWork toCreateOneTimeHouseWork() {
                return new CreateOneTimeHouseWork(
                        name,
                        houseWorkCategory,
                        0,
                        0,
                        assignedDate
                );
        }
}
