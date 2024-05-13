package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MemberErrorCode implements ErrorCode {
    DUPLICATED_EMAIL("아미 존재하는 이메일입니다."),
    NOT_FOUND("해당 유저가 존재하지 않습니다."),
    INVALID_LOGIN("이메일 또는 패스워드가 틀렸습니다."),
    ALREADY_HAS_HOUSE("참여중인 하우스가 있습니다."),
    NO_HAS_HOUSE("참여중인 하우스가 없습니다."),
    NOT_SAME_HOUSE("참여중인 하우스에 존재하지 않는 사용자입니다."),
    INVALID_HOUSE_MEMBER("참여중인 하우스가 아닙니다.")
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
