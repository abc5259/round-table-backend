package com.roundtable.roundtable.house.presentation.controller;

import com.roundtable.roundtable.global.config.argumentresolver.Login;
import com.roundtable.roundtable.global.presentation.response.Response;
import com.roundtable.roundtable.global.presentation.response.SuccessResponse;
import com.roundtable.roundtable.house.application.dto.CreateHouseRequest;
import com.roundtable.roundtable.house.application.service.HouseService;
import com.roundtable.roundtable.member.domain.Member;
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
    public ResponseEntity<Response<?>> createHouse(@Login Member loginMember, @Valid @RequestBody CreateHouseRequest createHouseRequest) {
        houseService.createHouse(createHouseRequest, loginMember);
        return ResponseEntity.ok().body(SuccessResponse.ok());
    }
}
