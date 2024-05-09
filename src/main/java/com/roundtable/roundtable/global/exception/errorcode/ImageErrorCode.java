package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    UPLOAD_ERROR("업로드 도중 문제가 생겼습니다", "image-001")
    ;

    private final String message;
    private final String code;

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public String getCode() {
        return "";
    }
}
