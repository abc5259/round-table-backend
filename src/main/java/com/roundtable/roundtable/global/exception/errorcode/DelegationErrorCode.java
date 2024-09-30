package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DelegationErrorCode implements ErrorCode {
    DELEGATION_FORBIDDEN_NOT_TODAY_SCHEDULE("오늘 수행하지 않는 스케줄에는 부탁을 할 수 없습니다.", "delegation-001"),
    DELEGATION_FORBIDDEN_ALREADY_COMPLETION_SCHEDULE("이미 완료된 스케줄에는 부탁을 할 수 없습니다.", "delegation-002"),
    ALREADY_EXIST_DELEGATION("이미 존재하는 부탁입니다.","delegation-003")
    ;

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
