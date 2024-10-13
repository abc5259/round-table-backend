package com.roundtable.roundtable.business.sse;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.domain.sse.SseEmitterId;
import com.roundtable.roundtable.domain.sse.SseEmitterRepository;
import com.roundtable.roundtable.domain.sse.SseEmitters;
import com.roundtable.roundtable.domain.sse.SseEvent;
import com.roundtable.roundtable.domain.sse.SseEventId;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseSubscribeService {

    private static final Long CONNECTION_TIME_OUT = 60 * 3 * 1000L;

    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(AuthMember authMember) {
        SseEmitterId sseEmitterId = SseEmitterId.of(authMember.houseId(), authMember.memberId(), LocalDateTime.now());
        SseEmitters emitter = sseEmitterRepository.save(sseEmitterId, new SseEmitter(CONNECTION_TIME_OUT));

        SseEventId sseEventId = SseEventId.of(authMember.houseId(), authMember.memberId(), LocalDateTime.now());
        emitter.sendEvent(sseEventId, SseEvent.CONNECTION);

        log.info("SSE 연결 생성 {}", sseEmitterId);
        return emitter.getSingleEmitter();
    }
}
