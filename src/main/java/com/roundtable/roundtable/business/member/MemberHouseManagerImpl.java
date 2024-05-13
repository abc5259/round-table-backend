package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.business.house.MemberHouseManager;
import com.roundtable.roundtable.business.house.dto.HouseMember;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.business.house.HouseReader;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberHouseManagerImpl implements MemberHouseManager {

    private final HouseReader houseReader;

    private final MemberReader memberReader;

    public void enterHouse(Long houseId, Long memberId) {
        House house = houseReader.findById(houseId);
        Member member = memberReader.findById(memberId);
        member.enterHouse(house);
    }

    public List<HouseMember> findHouseMembers(Long houseId) {
        List<Member> members = memberReader.findAllByHouseId(houseId);
        return members.stream()
                .map(member ->
                        new HouseMember(
                            member.getId(),
                            member.getName(),
                            member.getProfileUrl()
                        )
                )
                .toList();
    }
}
