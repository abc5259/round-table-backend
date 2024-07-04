package com.roundtable.roundtable.presentation.chore;

import com.roundtable.roundtable.business.chore.ChoreService;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/house/{houseId}/chores")
public class ChoreController {

    private final ChoreService choreService;

    @PatchMapping("/{choreId}/complete")
    public ResponseEntity<ApiResponse<Void>> completeChore(
            @Login AuthMember authMember,
            @PathVariable("choreId") Long choreId,
            @RequestPart("completedImage") MultipartFile completedImage) {

        choreService.completeChore(authMember, choreId, completedImage);

        return ResponseEntity.ok(SuccessResponse.ok());
    }
}
