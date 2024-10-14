package com.roundtable.roundtable.domain.sse.event;

import java.util.Set;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
public enum SimpleSseEvent implements SseEvent {
    CONNECTION("CONNECT", "성공적으로 연결되었습니다.");

    private final String eventName;
    private final String message;

    SimpleSseEvent(String eventName, String message) {
        this.eventName = eventName;
        this.message = message;
    }

    public Set<DataWithMediaType> createSendData(SseEventId sseEventId) {
        return SseEmitter.event()
                .id(sseEventId.toString())
                .name(this.eventName)
                .data(this.getMessage())
                .build();
    }
}
