package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScheduleCommentErrorCode implements ErrorCode {
    INVALID_CONTENT_LENGTH("길이는 유효하지 않은 길이의 content입니다."),
    NOT_SAME_HOUSE("member는 자신의 하우스에 있는 schdule에만 댓글을 달 수 있습니다.")
    ;

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return null;
    }
}
