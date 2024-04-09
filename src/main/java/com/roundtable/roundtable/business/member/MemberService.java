package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class  MemberService {

    private final MemberReader memberReader;
    private final MemberUpdater memberUpdater;

    public void settingProfile(final Long memberId, final MemberProfile memberProfile) {
        memberUpdater.settingProfile(memberId, memberProfile);
    }

    public boolean isExistEmail(String email) {
        return memberReader.existEmail(email);
    }
}
