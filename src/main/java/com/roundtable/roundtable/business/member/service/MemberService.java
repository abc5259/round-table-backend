package com.roundtable.roundtable.business.member.service;

import com.roundtable.roundtable.business.member.dto.ExistEmailRequest;
import com.roundtable.roundtable.business.member.dto.SettingProfileRequest;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class  MemberService {

    private final MemberRepository memberRepository;

    public void settingProfile(final Member loginMember, final SettingProfileRequest settingProfileRequest) {
        loginMember.settingProfile(settingProfileRequest.name(), settingProfileRequest.gender());
    }

    public boolean isExistEmail(ExistEmailRequest existEmailRequest) {
        return memberRepository.existsByEmail(existEmailRequest.email());
    }
}
