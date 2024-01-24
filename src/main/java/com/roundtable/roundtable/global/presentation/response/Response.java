package com.roundtable.roundtable.global.presentation.response;

import lombok.Getter;

@Getter
public class Response<T> {
    private final boolean success;
    private final T data;

    protected Response(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
