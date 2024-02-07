package com.roundtable.roundtable.business.housework;

import com.roundtable.roundtable.implement.housework.CreateOneTimeHouseWork;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.housework.CreateWeeklyHouseWork;
import com.roundtable.roundtable.implement.housework.HouseWorkMaker;
import com.roundtable.roundtable.implement.member.MemberReader;
import com.roundtable.roundtable.implement.member.MemberValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseWorkService {

    private final MemberReader memberReader;
    private final MemberValidator memberValidator;
    private final HouseWorkMaker houseWorkMaker;

    public Long createOneTimeHouseWork(
            Member loginMember,
            CreateOneTimeHouseWork createOneTimeHouseWork,
            List<Long> assignedMembersId
    ) {
        List<Member> assignedMembers = memberReader.findAllByIdInSameHouse(assignedMembersId, loginMember);

        return houseWorkMaker.createOneTimeHouseWork(
                loginMember.getHouse(),
                createOneTimeHouseWork,
                assignedMembers
        );
    }

    public Long createWeeklyHouseWork(
            Member loginMember,
            CreateWeeklyHouseWork createWeeklyHouseWork,
            List<Long> assignedMembersId
    ) {
        List<Member> assignedMembers = memberReader.findAllByIdInSameHouse(assignedMembersId, loginMember);

        return houseWorkMaker.createWeeklyHouseWork(
                loginMember.getHouse(),
                createWeeklyHouseWork,
                assignedMembers
        );
    }
}
