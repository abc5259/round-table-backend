package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_AUTH("유효하지 않은 인증입니다.")
    ;

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
