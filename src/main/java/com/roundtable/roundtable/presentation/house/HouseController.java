package com.roundtable.roundtable.presentation.house;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.presentation.house.request.CreateHouseRequest;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.business.house.HouseService;
import com.roundtable.roundtable.presentation.house.response.CreateHouseResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {

    private final HouseService houseService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createHouse(@Login AuthMember authMember, @Valid @RequestBody CreateHouseRequest createHouseRequest) {
        Long houseId = houseService.createHouse(createHouseRequest.toCreateHouse(), authMember);
        return ResponseEntity.ok(
                SuccessResponse.from(new CreateHouseResponse(houseId))
        );
    }

}
