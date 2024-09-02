package com.roundtable.roundtable.global.response;

import lombok.Getter;

@Getter
public class FailResponse<T> extends ResponseDto<T> {


    private FailResponse(String errorMessage, String code) {
        super(false, null, errorMessage, code);
    }

    private FailResponse(String errorMessage) {
        super(false, null, errorMessage, null);
    }

    public static <T> FailResponse<T> fail() {
        return new FailResponse<>(null);
    }

    public static <T> FailResponse<T> fail(String errorMessage) {
        return new FailResponse<>(errorMessage);
    }

    public static <T> FailResponse<T> fail(String errorMessage, String code) {
        return new FailResponse<>(errorMessage, code);
    }
}
