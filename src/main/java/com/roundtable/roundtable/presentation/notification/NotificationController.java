package com.roundtable.roundtable.presentation.notification;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.notification.NotificationService;
import com.roundtable.roundtable.global.response.ApiResponse;
import com.roundtable.roundtable.global.response.SuccessResponse;
import com.roundtable.roundtable.global.support.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/house/{houseId}/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Void>> findLoginUserNotification(
            @Login AuthMember authMember,
            @PathVariable Long houseId
            ) {

        return ResponseEntity.ok(SuccessResponse.ok());
    }
}
