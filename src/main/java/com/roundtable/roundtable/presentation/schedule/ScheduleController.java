package com.roundtable.roundtable.presentation.schedule;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.schedule.ScheduleService;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.presentation.schedule.request.CreateScheduleRequest;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house/{houseId}/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/repeat")
    public ResponseEntity<ApiResponse<Long>> createRepeatSchedule(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @Valid @RequestBody CreateScheduleRequest createScheduleRequest) {

        LocalDate now = LocalDate.now();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.from(
                        scheduleService.createSchedule(createScheduleRequest.toCreateScheduleDto(ScheduleType.REPEAT), authMember.toHouseAuthMember(houseId), now)
                )
        );
    }

    @PostMapping("/one-time")
    public ResponseEntity<ApiResponse<Long>> createOneTimeSchedule(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @Valid @RequestBody CreateScheduleRequest createScheduleRequest) {

        LocalDate now = LocalDate.now();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.from(
                        scheduleService.createSchedule(createScheduleRequest.toCreateScheduleDto(ScheduleType.ONE_TIME), authMember.toHouseAuthMember(houseId), now)
                )
        );
    }
}
