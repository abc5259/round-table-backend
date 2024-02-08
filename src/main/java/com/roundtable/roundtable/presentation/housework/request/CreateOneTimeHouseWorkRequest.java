package com.roundtable.roundtable.presentation.housework.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.roundtable.roundtable.implement.housework.CreateOneTimeHouseWork;
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
        @NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
        LocalDateTime assignedDate,
        @NotEmpty
        List<Long> assignedMembersId
        ) {
        public CreateOneTimeHouseWork toCreateOneTimeHouseWork() {
                return new CreateOneTimeHouseWork(
                        name,
                        houseWorkCategory,
                        1,
                        1,
                        assignedDate
                );
        }
}
