package com.roundtable.roundtable.business.member.dto;

import com.roundtable.roundtable.domain.member.Gender;

public record MemberProfile(
        String name,
        Gender gender
) {
}
