package com.roundtable.roundtable.presentation.sse;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.sse.SseSubscribeService;
import com.roundtable.roundtable.global.support.annotation.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {

    private final SseSubscribeService sseSubscribeService;

    @GetMapping(value = "/connect/house/{houseId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@Login AuthMember authMember, @PathVariable Long houseId) {
        SseEmitter sseEmitter = sseSubscribeService.subscribe(authMember.toHouseAuthMember(houseId));
        return ResponseEntity.ok(sseEmitter);
    }

    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@Login AuthMember authMember) {
        SseEmitter sseEmitter = sseSubscribeService.subscribe(authMember);
        return ResponseEntity.ok(sseEmitter);
    }
}
