package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {
    /**
     *  NOT FOUND
     */
    NOT_FOUND_ENTITY("엔티티가 존재하지 않습니다."),

    /**
     * CREATE
     */
    CRATE_ENTITY("엔터티를 생성하는데 문제가 발생했습니다."),

    /**
     * DUPLICATED
     */
    DUPLICATED("중복이 존재합니다.");

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
