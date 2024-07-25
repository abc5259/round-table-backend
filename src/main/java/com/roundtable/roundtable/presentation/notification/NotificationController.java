package com.roundtable.roundtable.presentation.notification;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.notification.NotificationService;
import com.roundtable.roundtable.business.notification.dto.response.NotificationResponse;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import com.roundtable.roundtable.presentation.common.request.CursorBasedPaginationRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house/{houseId}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<CursorBasedResponse<List<NotificationResponse>>>> findLoginUserNotification(
            @Login AuthMember authMember,
            @ModelAttribute CursorBasedPaginationRequest cursorBasedPaginationRequest
            ) {

        return ResponseEntity.ok(SuccessResponse.from(notificationService.findNotificationsByMemberId(cursorBasedPaginationRequest.toCursorBasedRequest(), authMember)));
    }
}
