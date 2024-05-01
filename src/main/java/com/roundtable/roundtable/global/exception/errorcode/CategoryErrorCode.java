package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {

    /**
     *  NOT FOUND
     */
    NOT_FOUND("카테고리 엔티티가 존재하지 않습니다."),


    /**
     * DUPLICATED
     */
    DUPLICATED_NAME("중복된 이름의 카테고리가 존재합니다.");

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
