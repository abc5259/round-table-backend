package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {
    NOT_FOUND_TOKEN("존재하지 않는 token 입니다.")
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
