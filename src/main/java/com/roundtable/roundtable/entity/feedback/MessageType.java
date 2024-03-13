package com.roundtable.roundtable.entity.feedback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {
    PROMISE_KEPT("정해진 약속을 잘 지켰어요"),
    TIME_KEPT("시간 약속을 잘 지켰어요");

    private final String description;
}
