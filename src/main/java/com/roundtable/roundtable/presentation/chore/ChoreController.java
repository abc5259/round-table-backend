package com.roundtable.roundtable.presentation.chore;

import com.roundtable.roundtable.business.chore.ChoreService;
import com.roundtable.roundtable.business.chore.dto.response.ChoreOfMemberResponse;
import com.roundtable.roundtable.business.chore.dto.response.ChoreResponse;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.common.request.CursorBasedPaginationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/house/{houseId}/chores")
public class ChoreController {

    private final ChoreService choreService;

    @Operation(summary = "잡안일 완료", description = "집안일을 완료합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PatchMapping("/{choreId}/complete")
    public ResponseEntity<ResponseDto<Void>> completeChore(
            @Login AuthMember authMember,
            @PathVariable("houseId") Long houseId,
            @PathVariable("choreId") Long choreId,
            @RequestPart("completedImage") MultipartFile completedImage) {

        choreService.completeChore(authMember.toHouseAuthMember(houseId), choreId, completedImage);

        return ResponseEntity.ok(SuccessResponse.ok());
    }

    @Operation(summary = "내 집안일 조회", description = "오늘 해야할 내 집안일을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/me")
    public ResponseEntity<ResponseDto<List<ChoreOfMemberResponse>>> findChoreOfLoginUser(
            @Login AuthMember authMember,
            @PathVariable("houseId") Long houseId,
            @RequestParam(required = false) LocalDate date
    ) {
        List<ChoreOfMemberResponse> chores = choreService.findChoresOfMember(authMember.toHouseAuthMember(houseId), date == null ? LocalDate.now() : date);

        return ResponseEntity.ok(SuccessResponse.from(chores));
    }

    @Operation(summary = "하우스 집안일 조회", description = "하우스의 오늘 집안일을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping
    public ResponseEntity<ResponseDto<CursorBasedResponse<List<ChoreResponse>>>> findChoreOfLoginUser(
            @Login AuthMember authMember,
            @PathVariable("houseId") Long houseId,
            @RequestParam(required = false) LocalDate date,
            @ModelAttribute CursorBasedPaginationRequest cursorBasedPaginationRequest
    ) {
        CursorBasedResponse<List<ChoreResponse>> choresOfHouse = choreService.findChoresOfHouse(
                date == null ? LocalDate.now() : date,
                houseId,
                cursorBasedPaginationRequest.toCursorBasedRequest()
        );

        return ResponseEntity.ok(SuccessResponse.from(choresOfHouse));
    }
}
