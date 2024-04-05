package com.roundtable.roundtable.presentation.auth.response;

public record LoginResponse (
        String accessToken,
        String refreshToken
) {

}
