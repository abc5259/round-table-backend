package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.housework.HouseWork;
import com.roundtable.roundtable.entity.housework.HouseWorkMember;
import com.roundtable.roundtable.entity.housework.HouseWorkMemberRepository;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseWorkMemberMaker {
    private static final int SEQUENCE = 1;
    private final HouseWorkMemberRepository houseWorkMemberRepository;

    public void createOneTimeHouseWorkMembers(House house, HouseWork houseWork, List<Member> assignedMembers) {
        List<HouseWorkMember> houseWorkMembers = assignedMembers.stream().map(member -> new HouseWorkMember(
                SEQUENCE,
                member,
                house,
                houseWork
        )).toList();

        houseWorkMemberRepository.saveAll(houseWorkMembers);
    }

    public void createWeeklyHouseWorkMembers(House house, HouseWork houseWork, List<Member> assignedMembers) {
        AtomicInteger index = new AtomicInteger();

        List<HouseWorkMember> houseWorkMembers = assignedMembers.stream().map(member -> new HouseWorkMember(
                index.getAndIncrement() + 1,
                member,
                house,
                houseWork
        )).toList();

        houseWorkMemberRepository.saveAll(houseWorkMembers);
    }
}
