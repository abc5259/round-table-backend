package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ChoreErrorCode implements ErrorCode {
    ALREADY_COMPLETED_CHORE("이미 완료된 집안일입니다.", "chore-001"),
    NOT_COMPLETED_CHORE("완료되지 않은 집안일입니다.", "chore-002");

    private final String message;
    private final String code;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return code;
    }
}
