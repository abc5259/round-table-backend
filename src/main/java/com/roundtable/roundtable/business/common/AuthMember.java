package com.roundtable.roundtable.business.common;

public record AuthMember(
        Long memberId,
        Long houseId
) {
    public AuthMember(Long memberId) {
        this(memberId, null);
    }

    public AuthMember toHouseAuthMember(Long houseId) {
        return new AuthMember(this.memberId, houseId);
    }
}
