package com.roundtable.roundtable.member.application.service;

import com.roundtable.roundtable.house.domain.House;
import com.roundtable.roundtable.house.domain.HouseRepository;
import com.roundtable.roundtable.house.exception.HouseException;
import com.roundtable.roundtable.house.exception.HouseException.HouseNotFoundException;
import com.roundtable.roundtable.member.application.dto.ExistEmailRequest;
import com.roundtable.roundtable.member.application.dto.SettingProfileRequest;
import com.roundtable.roundtable.member.domain.Member;
import com.roundtable.roundtable.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    private final HouseRepository houseRepository;

    public void settingProfile(final Member loginMember, final SettingProfileRequest settingProfileRequest) {
        log.info("member = " + loginMember.getId());
        loginMember.settingProfile(settingProfileRequest.name(), settingProfileRequest.gender());
    }

    public boolean isExistEmail(ExistEmailRequest existEmailRequest) {
        return memberRepository.existsByEmail(existEmailRequest.email());
    }

    public void enterHouse(Long houseId, Member loginMember) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(HouseNotFoundException::new);

        loginMember.enterHouse(house);
    }
}
