package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    INVALID_AUTH("유효하지 않은 인증입니다."),
    NOT_FOUND_AUTH_HEADER("인증에 사용할 header 가 빈 값입니다."),
    NOT_START_HEADER_BEAR("인증 header 가 Bear로 시작하지 않습니다."),
    NO_FOUND_TOKEN("인증 header에 access token이 존재하지 않습니다."),
    JWT_EXTRACT_ERROR("jwt 토큰을 추출하는데 문제가 생겼습니다."),
    JWT_EXPIRED_ERROR("jwt 토큰의 유효기간이 지났습니다.")
    ;

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
}
