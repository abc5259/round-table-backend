package com.roundtable.roundtable.domain.sse.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
public abstract class SseEventTemplate<T> implements SseEvent {

    private final T eventData;

    protected SseEventTemplate(T eventData) {
        this.eventData = eventData;
    }

    public abstract String getEventName();

    @Override
    public String getMessage() {
        return eventData.toString();
    }

    @Override
    public Set<DataWithMediaType> createSendData(SseEventId sseEventId) {
        return SseEmitter.event()
                .id(sseEventId.toString())
                .name(getEventName())
                .data(extractEventDataAsJson())
                .build();
    }

    private String extractEventDataAsJson() {
        final String jsonData;
        try {
            jsonData = objectMapper.writeValueAsString(eventData);
        } catch (JsonProcessingException e) {
            log.error("SSE data json parsing Exception - {}", eventData, e);
            throw new IllegalArgumentException(e);
        }
        return jsonData;
    }
}

