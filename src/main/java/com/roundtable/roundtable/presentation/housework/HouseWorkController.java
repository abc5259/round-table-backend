package com.roundtable.roundtable.presentation.housework;

import com.roundtable.roundtable.business.housework.HouseWorkService;
import com.roundtable.roundtable.business.housework.response.HouseWorkResponse;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.presentation.housework.request.CreateOneTimeHouseWorkRequest;
import com.roundtable.roundtable.presentation.housework.request.CreateWeeklyHouseWorkRequest;
import com.roundtable.roundtable.presentation.support.argumentresolver.Login;
import com.roundtable.roundtable.presentation.support.response.Response;
import com.roundtable.roundtable.presentation.support.response.SuccessResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/houseworks")
@RequiredArgsConstructor
public class HouseWorkController {

    private final HouseWorkService houseWorkService;

    @PostMapping("/one-time")
    public ResponseEntity<Response<?>> createHouseWork(
            @Login Member member,
            @Valid  @RequestBody CreateOneTimeHouseWorkRequest createOneTimeHouseWorkRequest) {
        Long oneTimeHouseWorkId = houseWorkService.createOneTimeHouseWork(
                member,
                createOneTimeHouseWorkRequest.toCreateOneTimeHouseWork(),
                createOneTimeHouseWorkRequest.assignedMembersId()
        );

        return ResponseEntity.ok().body(SuccessResponse.from(oneTimeHouseWorkId));
    }

    @PostMapping("/weekly")
    public ResponseEntity<Response<?>> createWeeklyHouseWork(
            @Login Member member,
            @Valid  @RequestBody CreateWeeklyHouseWorkRequest createWeeklyHouseWorkRequest) {
        Long oneTimeHouseWorkId = houseWorkService.createWeeklyHouseWork(
                member,
                createWeeklyHouseWorkRequest.toCreateWeeklyHouseWork(),
                createWeeklyHouseWorkRequest.assignedMembersId()
        );

        return ResponseEntity.ok().body(SuccessResponse.from(oneTimeHouseWorkId));
    }

    @GetMapping()
    public ResponseEntity<Response<List<HouseWorkResponse>>> findHouseWorksByDate(
            @Login Member member,
            @RequestParam("targetDate") LocalDate targetDate
            ) {
        return ResponseEntity.ok()
                .body(SuccessResponse.from(houseWorkService.findHouseWorksByDate(targetDate,member)));
    }
}
