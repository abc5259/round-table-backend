package com.roundtable.roundtable.presentation.house;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.presentation.house.request.CreateHouseRequest;
import com.roundtable.roundtable.presentation.argumentresolver.Login;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.business.house.HouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createHouse(@Login Member loginMember, @Valid @RequestBody CreateHouseRequest createHouseRequest) {
        houseService.createHouse(createHouseRequest.toCreateHouse(), loginMember);
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }
}
