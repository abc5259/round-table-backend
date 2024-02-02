package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.member.MemberProfile;
import com.roundtable.roundtable.implement.member.MemberReader;
import com.roundtable.roundtable.implement.member.MemberUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class  MemberService {

    private final MemberReader memberReader;
    private final MemberUpdater memberUpdater;

    public void settingProfile(final Member loginMember, final MemberProfile memberProfile) {
        memberUpdater.settingProfile(loginMember, memberProfile);
    }

    public boolean isExistEmail(String email) {
        return memberReader.isExistEmail(email);
    }
}
