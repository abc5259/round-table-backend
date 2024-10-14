package com.roundtable.roundtable.domain.sse;

import java.util.Set;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter.DataWithMediaType;

public interface SseEvent {
    String getEventName();
    String getMessage();
    Set<DataWithMediaType> createSendData(SseEventId sseEventId);
}
