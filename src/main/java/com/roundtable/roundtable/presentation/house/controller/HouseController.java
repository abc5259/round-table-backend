package com.roundtable.roundtable.presentation.house.controller;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.presentation.support.config.argumentresolver.Login;
import com.roundtable.roundtable.presentation.support.response.Response;
import com.roundtable.roundtable.presentation.support.response.SuccessResponse;
import com.roundtable.roundtable.business.house.dto.CreateHouseRequest;
import com.roundtable.roundtable.business.house.service.HouseService;
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
