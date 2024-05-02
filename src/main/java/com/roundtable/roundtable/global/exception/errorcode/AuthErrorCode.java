package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_AUTH_USER("인증되지 않은 사용자입니다.", "auth-001"),
    INVALID_AUTH("유효하지 않은 인증입니다.", "auth-001"),
    NOT_FOUND_AUTH_HEADER("인증에 사용할 header 가 빈 값입니다.", "auth-002"),
    NOT_START_HEADER_BEAR("인증 header 가 Bear로 시작하지 않습니다.", "auth-003"),
    NO_FOUND_TOKEN("인증 header에 access token이 존재하지 않습니다.", "auth-004"),
    JWT_EXTRACT_ERROR("jwt 토큰을 추출하는데 문제가 생겼습니다.", "auth-005"),
    JWT_EXPIRED_ERROR("jwt 토큰의 유효기간이 지났습니다.", "auth-006"),
    EMAIL_NOT_VERIFIED("이메일 인증이 되지 않았습니다.", "auth-007")
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
