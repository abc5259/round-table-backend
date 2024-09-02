package com.roundtable.roundtable.presentation.house;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.house.dto.HouseMember;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.presentation.house.request.CreateHouseRequest;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.business.house.HouseService;
import com.roundtable.roundtable.presentation.house.response.CreateHouseResponse;
import com.roundtable.roundtable.presentation.house.response.HouseMemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @Operation(summary = "하우스 생성", description = "하우스를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping
    public ResponseEntity<ResponseDto<?>> createHouse(@Login AuthMember authMember, @Valid @RequestBody CreateHouseRequest createHouseRequest) {
        Long houseId = houseService.createHouse(createHouseRequest.toCreateHouse(), authMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.from(new CreateHouseResponse(houseId)));
    }

    @Operation(summary = "하우스의 전체 맴버 조회", description = "하우스의 전체 맴버를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/{houseId}")
    public ResponseEntity<ResponseDto<?>> findHouseMembers(@Login AuthMember authMember, @PathVariable Long houseId) {

        List<HouseMember> houseMembers = houseService.findHouseMembers(authMember.toHouseAuthMember(houseId));
        List<HouseMemberResponse> response = houseMembers.stream().map(HouseMemberResponse::new).toList();

        return ResponseEntity.ok(
                SuccessResponse.from(response)
        );
    }
}
