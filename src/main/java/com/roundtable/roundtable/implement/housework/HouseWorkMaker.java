package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.housework.CreateOneTimeHouseWork;
import com.roundtable.roundtable.entity.housework.HouseWorkMember;
import com.roundtable.roundtable.entity.housework.HouseWorkMemberMaker;
import com.roundtable.roundtable.entity.housework.HouseWorkMemberRepository;
import com.roundtable.roundtable.entity.housework.HouseWorkRepository;
import com.roundtable.roundtable.entity.housework.OneTimeHouseWork;
import com.roundtable.roundtable.entity.member.Member;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseWorkMaker {

    private final HouseWorkRepository houseWorkRepository;
    private final HouseWorkMemberMaker houseWorkMemberMaker;

    public Long createOneTimeHouseWork(House house, CreateOneTimeHouseWork createOneTimeHouseWork, List<Member> assignedMembers) {
        OneTimeHouseWork oneTimeHouseWork = new OneTimeHouseWork(
                createOneTimeHouseWork.name(),
                createOneTimeHouseWork.houseWorkCategory(),
                createOneTimeHouseWork.currSequence(),
                createOneTimeHouseWork.sequenceSize(),
                createOneTimeHouseWork.assignedDate()
        );

        OneTimeHouseWork savedOneTimeHouseWork = houseWorkRepository.save(oneTimeHouseWork);

        houseWorkMemberMaker.createHouseWorkMembers(house, savedOneTimeHouseWork, assignedMembers);

        return savedOneTimeHouseWork.getId();
    }
}
