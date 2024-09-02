package com.roundtable.roundtable.presentation.notification;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.notification.NotificationService;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.global.response.ResponseDto;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.common.request.CursorBasedPaginationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/house/{houseId}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "특정 하우스에서의 알림 조회", description = "요청 param으로 온 houseId에서의 알림을 확인합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/house/{houseId}/notifications")
    public ResponseEntity<ResponseDto<CursorBasedResponse<List<NotificationResponse>>>> findLoginUserNotification(
            @Login AuthMember authMember,
            @PathVariable Long houseId,
            @ModelAttribute CursorBasedPaginationRequest cursorBasedPaginationRequest
            ) {

        return ResponseEntity.ok(
                SuccessResponse.from(
                        notificationService.findNotificationsByMemberId(
                                cursorBasedPaginationRequest.toCursorBasedRequest(),
                                authMember.toHouseAuthMember(houseId)
                        )
                )
        );
    }
}
