package com.roundtable.roundtable.presentation.house;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.dto.HouseMember;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.presentation.house.request.CreateHouseRequest;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.business.house.HouseService;
import com.roundtable.roundtable.presentation.house.response.CreateHouseResponse;
import com.roundtable.roundtable.presentation.house.response.HouseMemberResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{houseId}")
    public ResponseEntity<ApiResponse<?>> findHouseMembers(@Login AuthMember authMember, @PathVariable Long houseId) {

        List<HouseMember> houseMembers = houseService.findHouseMembers(authMember.toHouseAuthMember(houseId));
        List<HouseMemberResponse> response = houseMembers.stream().map(HouseMemberResponse::new).toList();

        return ResponseEntity.ok(
                SuccessResponse.from(response)
        );
    }
}
