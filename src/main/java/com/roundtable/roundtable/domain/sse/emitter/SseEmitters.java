package com.roundtable.roundtable.domain.sse.emitter;

import com.roundtable.roundtable.domain.sse.event.SseEvent;
import com.roundtable.roundtable.domain.sse.event.SseEventId;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Getter
public class SseEmitters {

    private final Map<SseEmitterId, SseEmitter> emitters;

    public SseEmitters(Map<SseEmitterId, SseEmitter> emitters) {
        this.emitters = emitters;
    }

    public void sendEvent(SseEvent sseEvent, LocalDateTime sendTime) {
        emitters.forEach((emitterId, emitter) -> sendToEmitter(emitterId, emitter, sseEvent, sendTime));
    }

    private void sendToEmitter(SseEmitterId emitterId, SseEmitter emitter, SseEvent sseEvent, LocalDateTime sendTime) {
        log.info("SSE 메시지 보내기 시도 : {}", emitterId);
        SseEventId sseEventId = emitterId.toSseEventId(sendTime);
        try {
            emitter.send(sseEvent.createSendData(sseEventId));
            log.info("SSE 메시지 보내기 성공 : {}, event : {}, message: {}", sseEventId, sseEvent.getEventName(), sseEvent.getMessage());
        } catch (IOException e) {
            log.error("SSE 메시지 보내기 실패 : {}, error : {}", e.getMessage(), e.getClass());
            emitter.complete();
        }
    }

    public SseEmitter getSingleEmitter() {
        if (emitters.size() != 1) {
            throw new IllegalStateException(String.format("생성된 SseEmitter가 1개가 아닙니다. emitter수 : %d", emitters.size()));
        }
        return emitters.values().stream()
                .findFirst()
                .orElseThrow();
    }

}
