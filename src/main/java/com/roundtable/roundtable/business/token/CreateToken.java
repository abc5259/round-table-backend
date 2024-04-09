package com.roundtable.roundtable.business.token;

public record CreateToken(
        Long memberId,
        String refreshToken
) {
}
