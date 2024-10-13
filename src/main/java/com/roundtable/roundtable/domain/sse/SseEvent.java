package com.roundtable.roundtable.domain.sse;

import lombok.Getter;

@Getter
public enum SseEvent {
    CONNECTION("CONNECT", "성공적으로 연결되었습니다.");

    private final String eventName;
    private final String message;

    SseEvent(String eventName, String message) {
        this.eventName = eventName;
        this.message = message;
    }
}
