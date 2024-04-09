package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.domain.member.Gender;

public record MemberProfile(
        String name,
        Gender gender
) {
}
