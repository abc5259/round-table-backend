package com.roundtable.roundtable.domain.sse;

import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
public class SseEmitters {

    private final Map<SseEmitterId, SseEmitter> emitters;

    public SseEmitters(Map<SseEmitterId, SseEmitter> emitters) {
        this.emitters = emitters;
    }

    public void sendEvent(SseEventId sseEventId, SseEvent sseEvent) {
        emitters.forEach((emitterId, emitter) -> {
            sendToEmitter(emitterId, emitter, sseEventId, sseEvent);
        });
    }

    private void sendToEmitter(SseEmitterId emitterId, SseEmitter emitter, SseEventId sseEventId, SseEvent sseEvent) {
        log.info("SSE 메시지 보내기 시도 : {}", emitterId);
        try {
            emitter.send(SseEmitter.event().id(sseEventId.toString())
                    .name(sseEvent.getEventName())
                    .data(sseEvent.getEventName())
            );
            log.info("SSE 메시지 보내기 성공 : {}, event : {}", sseEventId, sseEvent.getEventName());
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
