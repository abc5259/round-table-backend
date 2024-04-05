package com.roundtable.roundtable.business.auth;

public record JwtPayload(
        Long userId,
        Long houseId
) {
}
