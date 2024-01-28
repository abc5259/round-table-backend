package com.roundtable.roundtable.member.application.service;

import com.roundtable.roundtable.house.application.service.HouseService;
import com.roundtable.roundtable.house.domain.House;
import com.roundtable.roundtable.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberHouseService {
    private final MemberService memberService;
    private final HouseService houseService;

    public void enterHouse(Long houseId, Member loginMember) {
        House house = houseService.findById(houseId);

        loginMember.enterHouse(house);
    }


}
