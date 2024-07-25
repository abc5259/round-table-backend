package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FeedbackErrorCode implements ErrorCode {

    NOT_FOUND_PREDEFINED_FEEDBACK("존재하지 않는 predefined feedback입니다.", "feedback-001")
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
