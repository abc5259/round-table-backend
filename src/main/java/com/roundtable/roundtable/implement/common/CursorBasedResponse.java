package com.roundtable.roundtable.implement.common;

import lombok.Getter;

@Getter
public class CursorBasedResponse<T> {
    private final T content;
    private final Long lastCursorId;

    private CursorBasedResponse(T content, Long lastCursorId) {
        this.content = content;
        this.lastCursorId = lastCursorId;
    }

    public static <T>CursorBasedResponse<T> of(T content, Long lastCursorId) {
        return new CursorBasedResponse<>(content, lastCursorId);
    }
}
