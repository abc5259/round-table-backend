package com.roundtable.roundtable.business.common;

public record AuthMember(
        Long memberId,
        Long houseId
) {
    public AuthMember(Long memberId) {
        this(memberId, null);
    }
}
