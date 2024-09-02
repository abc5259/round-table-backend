package com.roundtable.roundtable.global.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDto<T> {
    private final boolean success;
    private final T data;
    private String message;
    private String code;

    @Builder
    protected ResponseDto(boolean success, T data, String message, String code) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.code = code;
    }

    protected ResponseDto(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
