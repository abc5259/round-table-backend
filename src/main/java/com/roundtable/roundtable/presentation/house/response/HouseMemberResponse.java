package com.roundtable.roundtable.presentation.house.response;

import com.roundtable.roundtable.business.house.dto.HouseMember;

public record HouseMemberResponse(
        Long memberId,
        String name,
        String profileUrl
) {
    public HouseMemberResponse(HouseMember houseMember) {
        this(houseMember.memberId(), houseMember.name(), houseMember.profileUrl());
    }
}
