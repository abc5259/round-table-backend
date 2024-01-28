package com.roundtable.roundtable.member.application.service;

import com.roundtable.roundtable.member.application.dto.ExistEmailRequest;
import com.roundtable.roundtable.member.application.dto.SettingProfileRequest;
import com.roundtable.roundtable.member.domain.Member;
import com.roundtable.roundtable.member.domain.MemberRepository;
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
