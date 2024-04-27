package com.roundtable.roundtable.global.response;

import lombok.Getter;

@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private String message;

    protected ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    protected ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
