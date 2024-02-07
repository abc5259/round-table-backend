package com.roundtable.roundtable.presentation.housework.request;

import com.roundtable.roundtable.entity.housework.HouseWorkCategory;
import com.roundtable.roundtable.entity.housework.HouseWorkDivision;
import com.roundtable.roundtable.implement.housework.CreateOneTimeHouseWork;
import com.roundtable.roundtable.implement.housework.CreateWeeklyHouseWork;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record CreateWeeklyHouseWorkRequest(
        @NotBlank
        String name,
        @NotNull
        HouseWorkCategory houseWorkCategory,
        @NotEmpty
        List<Long> dayIds,
        @NotNull
        LocalTime assignedTime,
        @NotNull
        HouseWorkDivision houseWorkDivision,
        @NotEmpty
        List<Long> assignedMembersId
        ) {
        public CreateWeeklyHouseWork toCreateWeeklyHouseWork() {
                return new CreateWeeklyHouseWork(
                        name,
                        houseWorkCategory,
                        dayIds,
                        assignedTime,
                        houseWorkDivision,
                        1,
                        assignedMembersId.size()
                );
        }
}
