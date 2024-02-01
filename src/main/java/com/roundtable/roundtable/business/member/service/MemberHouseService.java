package com.roundtable.roundtable.business.member.service;

import com.roundtable.roundtable.business.house.service.HouseService;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
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
