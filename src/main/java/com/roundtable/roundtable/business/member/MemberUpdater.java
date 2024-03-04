package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberUpdater {

    public void settingProfile(final Member member, final MemberProfile memberProfile) {
        member.settingProfile(memberProfile.name(), memberProfile.gender());
    }

}
