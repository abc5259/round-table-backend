package com.roundtable.roundtable.domain.sse;

import com.roundtable.roundtable.domain.sse.event.SseEvent;
import com.roundtable.roundtable.domain.sse.event.SseEventId;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class TestSseEvent implements SseEvent {
    @Override
    public String getEventName() {
        return "test";
    }

    @Override
    public String getMessage() {
        return "test message";
    }

    @Override
    public Set<DataWithMediaType> createSendData(SseEventId sseEventId) {
        return SseEmitter.event()
                .id(sseEventId.toString())
                .name(getEventName())
                .data(getMessage())
                .build();
    }
}
