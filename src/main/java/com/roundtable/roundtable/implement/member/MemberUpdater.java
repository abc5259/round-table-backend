package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberUpdater {

    public void settingProfile(final Member member, final MemberProfile memberProfile) {
        member.settingProfile(memberProfile.name(), memberProfile.gender());
    }

}
