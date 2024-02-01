package com.roundtable.roundtable.presentation.support.response;

import lombok.Getter;

@Getter
public class ErrorResponse<T> extends Response<T> {

    private final String errorMessage;

    private ErrorResponse(String errorMessage) {
        super(false, null);
        this.errorMessage = errorMessage;
    }

    public static <T> ErrorResponse<T> fail() {
        return new ErrorResponse<>(null);
    }

    public static <T> ErrorResponse<T> fail(String errorMessage) {
        return new ErrorResponse<>(errorMessage);
    }
}
