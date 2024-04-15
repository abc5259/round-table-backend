package com.roundtable.roundtable.business.token.dto;

public record CreateToken(
        Long memberId,
        String refreshToken
) {
}
