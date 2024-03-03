package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScheduleCommentErrorCode implements ErrorCode {
    INVALID_CONTENT_LENGTH("길이는 유효하지 않은 길이의 content입니다.")
    ;

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
