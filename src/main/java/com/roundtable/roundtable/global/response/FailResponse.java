package com.roundtable.roundtable.global.response;

import lombok.Getter;

@Getter
public class FailResponse<T> extends ApiResponse<T> {

    private final String errorMessage;

    private FailResponse(String errorMessage) {
        super(false, null);
        this.errorMessage = errorMessage;
    }

    public static <T> FailResponse<T> fail() {
        return new FailResponse<>(null);
    }

    public static <T> FailResponse<T> fail(String errorMessage) {
        return new FailResponse<>(errorMessage);
    }
}
