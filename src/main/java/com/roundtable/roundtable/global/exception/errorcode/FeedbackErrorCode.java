package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FeedbackErrorCode implements ErrorCode {

    NOT_FOUND_PREDEFINED_FEEDBACK("존재하지 않는 predefined feedback입니다.", "feedback-001"),
    NOT_COMPLETION_SCHEDULE("완료되지 않은 스케줄에는 피드백을 보낼 수 없습니다.", "feedback-002")
    ;

    private final String message;
    private final String code;

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public String getCode() {
        return "";
    }
}
