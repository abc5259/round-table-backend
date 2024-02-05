package com.roundtable.roundtable.presentation.housework;

import com.roundtable.roundtable.business.housework.HouseWorkService;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.presentation.housework.request.CreateOneTimeHouseWorkRequest;
import com.roundtable.roundtable.presentation.support.argumentresolver.Login;
import com.roundtable.roundtable.presentation.support.response.Response;
import com.roundtable.roundtable.presentation.support.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
