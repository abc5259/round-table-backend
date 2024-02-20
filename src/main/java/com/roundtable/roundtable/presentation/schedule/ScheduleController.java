package com.roundtable.roundtable.presentation.schedule;

import com.roundtable.roundtable.business.schedule.ScheduleService;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.presentation.schedule.request.CreateScheduleRequest;
import com.roundtable.roundtable.presentation.support.argumentresolver.Login;
import com.roundtable.roundtable.presentation.support.response.Response;
import com.roundtable.roundtable.presentation.support.response.SuccessResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ResponseEntity<Response<Long>> createSchedule(
            @Login Member loginMember,
            @Valid @RequestBody CreateScheduleRequest createScheduleRequest) {

        LocalDate now = LocalDate.now();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                SuccessResponse.from(
                        scheduleService.createSchedule(createScheduleRequest.toCreateScheduleDto(), loginMember, now)
                )
        );
    }
}
