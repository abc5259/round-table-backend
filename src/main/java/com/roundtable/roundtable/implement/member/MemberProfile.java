package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.member.Gender;

public record MemberProfile(
        String name,
        Gender gender
) {
}
