package com.roundtable.roundtable.domain.sse.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;

public interface SseEvent {
    ObjectMapper objectMapper = new ObjectMapper();

    String getEventName();
    String getMessage();
    Set<DataWithMediaType> createSendData(SseEventId sseEventId);
}
