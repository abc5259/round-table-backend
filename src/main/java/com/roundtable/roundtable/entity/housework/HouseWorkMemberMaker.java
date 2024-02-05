package com.roundtable.roundtable.entity.housework;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseWorkMemberMaker {
    private final HouseWorkMemberRepository houseWorkMemberRepository;

    public void createHouseWorkMembers(House house, HouseWork houseWork, List<Member> assignedMembers) {
        List<HouseWorkMember> houseWorkMembers = assignedMembers.stream().map(member -> new HouseWorkMember(
                1,
                member,
                house,
                houseWork
        )).toList();

        houseWorkMemberRepository.saveAll(houseWorkMembers);
    }
}
