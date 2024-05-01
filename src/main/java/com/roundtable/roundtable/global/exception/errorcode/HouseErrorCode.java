package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum HouseErrorCode implements ErrorCode{

    /**
     *  NOT FOUND
     */
    NOT_FOUND("해당 하우스가가 존재하지 않습니다."),
    DUPLICATED_INVITE_CODE("중복된 초대 코드가 있습니다.");

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
