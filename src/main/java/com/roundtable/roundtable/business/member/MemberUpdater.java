package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberUpdater {

    private final MemberReader memberReader;

    public void settingProfile(final Long memberId, final MemberProfile memberProfile) {
        Member member = memberReader.findById(memberId);
        member.settingProfile(memberProfile.name(), memberProfile.gender());
    }

}
