package com.roundtable.roundtable.presentation.delegation;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.delegation.DelegationService;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.delegation.request.CreateDelegationRequest;
import com.roundtable.roundtable.presentation.delegation.request.UpdateDelegationRequest;
import com.roundtable.roundtable.presentation.delegation.response.CreateDelegationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses/{houseId}/delegation")
public class DelegationController {

    private final DelegationService delegationService;

    @Operation(summary = "집안일 부탁하기", description = "집안일을 부탁합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping
    public ResponseEntity<ResponseDto<CreateDelegationResponse>> createDelegation(
            @PathVariable Long houseId,
            @Login AuthMember authMember,
            @Valid @RequestBody CreateDelegationRequest createDelegationRequest
            ) {
        Long delegationId = delegationService.createDelegation(
                houseId,
                createDelegationRequest.toCreateDelegationDto(authMember, LocalDate.now())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.from(new CreateDelegationResponse(delegationId)));
    }

    @Operation(summary = "집안일 부탁 수락", description = "집안일 부탁을 수락합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PatchMapping("/{delegationId}/approve")
    public ResponseEntity<ResponseDto<Void>> approveDelegation(
            @PathVariable Long houseId,
            @PathVariable Long delegationId,
            @Login AuthMember authMember
            ) {
        delegationService.approveDelegation(houseId, authMember.memberId(), delegationId, LocalDate.now());
        return ResponseEntity.ok(SuccessResponse.ok());
    }

    @Operation(summary = "집안일 부탁 거절", description = "집안일 부탁을 거절합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PatchMapping("/{delegationId}/reject")
    public ResponseEntity<ResponseDto<Void>> rejectDelegation(
            @PathVariable Long houseId,
            @PathVariable Long delegationId,
            @Login AuthMember authMember
    ) {
        delegationService.rejectDelegation(houseId, authMember.memberId(), delegationId, LocalDate.now());
        return ResponseEntity.ok(SuccessResponse.ok());
    }
}
