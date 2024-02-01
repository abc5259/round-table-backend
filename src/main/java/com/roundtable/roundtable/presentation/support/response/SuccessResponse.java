package com.roundtable.roundtable.presentation.support.response;

import lombok.Getter;

@Getter
public class SuccessResponse<T> extends Response<T> {

    private SuccessResponse() {
        super(true, null);
    }

    private SuccessResponse(T data) {
        super(true, data);
    }

    public static <T> SuccessResponse<T> from(T data) {
        return new SuccessResponse<>(data);
    }

    public static <T> SuccessResponse<T> ok() {
        return new SuccessResponse<>();
    }
}
