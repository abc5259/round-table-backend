package com.roundtable.roundtable.business.member.dto.response;

import com.roundtable.roundtable.domain.member.Gender;

public record MemberDetailResponse(
        Long memberId,
        String name,
        Gender gender,
        HouseDetailResponse house
) {
}
