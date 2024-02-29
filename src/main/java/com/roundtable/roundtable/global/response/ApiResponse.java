package com.roundtable.roundtable.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;

    protected ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
