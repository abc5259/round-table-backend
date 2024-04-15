package com.roundtable.roundtable.business.auth.dto;

public record JwtPayload(
        Long userId,
        Long houseId
) {
}
