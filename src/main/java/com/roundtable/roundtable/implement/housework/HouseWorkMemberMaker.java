package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.housework.HouseWork;
import com.roundtable.roundtable.entity.housework.HouseWorkDivision;
import com.roundtable.roundtable.entity.housework.HouseWorkMember;
import com.roundtable.roundtable.entity.housework.HouseWorkMemberRepository;
import com.roundtable.roundtable.entity.housework.OneTimeHouseWork;
import com.roundtable.roundtable.entity.housework.WeeklyHouseWork;
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
    public static final int START_SEQUENCE = 1;
    private final HouseWorkMemberRepository houseWorkMemberRepository;

    public void createOneTimeHouseWorkMembers(OneTimeHouseWork oneTimeHouseWork, List<Member> assignedMembers) {
        List<HouseWorkMember> houseWorkMembers = toHouseWorkMembersWithFixSequence(assignedMembers, oneTimeHouseWork);

        houseWorkMemberRepository.saveAll(houseWorkMembers);
    }

    public void createWeeklyHouseWorkMembers(WeeklyHouseWork weeklyHouseWork, List<Member> assignedMembers) {

        List<HouseWorkMember> houseWorkMembers;

        if(weeklyHouseWork.getHouseWorkDivision() == HouseWorkDivision.FIX) {
            houseWorkMembers =  toHouseWorkMembersWithFixSequence(assignedMembers, weeklyHouseWork);
        }else {
            houseWorkMembers = toHouseWorkMembersWithIncreaseSequence(assignedMembers, weeklyHouseWork);
        }

        houseWorkMemberRepository.saveAll(houseWorkMembers);
    }

    private static List<HouseWorkMember> toHouseWorkMembersWithFixSequence(List<Member> assignedMembers,
                                                                           HouseWork houseWork) {
        return assignedMembers.stream().map(member -> new HouseWorkMember(
                START_SEQUENCE,
                member,
                houseWork
        )).toList();
    }

    private static List<HouseWorkMember> toHouseWorkMembersWithIncreaseSequence(List<Member> assignedMembers,
                                                                                HouseWork houseWork) {
        AtomicInteger index = new AtomicInteger(START_SEQUENCE);

        return assignedMembers.stream().map(member -> new HouseWorkMember(
                index.getAndIncrement() + 1,
                member,
                houseWork
        )).toList();
    }
}
