package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.entity.member.Gender;

public record MemberProfile(
        String name,
        Gender gender
) {
}
