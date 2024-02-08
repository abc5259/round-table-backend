package com.roundtable.roundtable.business.housework;

import com.roundtable.roundtable.business.housework.response.HouseWorkResponse;
import com.roundtable.roundtable.entity.housework.HouseWork;
import com.roundtable.roundtable.implement.housework.CreateOneTimeHouseWork;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.housework.CreateWeeklyHouseWork;
import com.roundtable.roundtable.implement.housework.HouseWorkMaker;
import com.roundtable.roundtable.implement.housework.HouseWorkReader;
import com.roundtable.roundtable.implement.member.MemberReader;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseWorkService {

    private final MemberReader memberReader;
    private final HouseWorkMaker houseWorkMaker;
    private final HouseWorkReader houseWorkReader;

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

    public List<HouseWorkResponse> findHouseWorksByDate(LocalDate targetDate, Member member) {
        List<HouseWork> houseWorks = houseWorkReader.findHouseWorksByDate(targetDate, member.getHouse());

        // houseWorks의 id를 가지고있는 houseWorkMember 테이블을 조회후
        // houseWorkMember을 java로 for문을 돌면서 각각의 housework_id에 맞는 sequence값이 houseWork의 currSequence에 맞는거만 걸러낸다.
        return houseWorks.stream()
                .map(houseWork -> new HouseWorkResponse(
                        houseWork.getId(),
                        houseWork.getName(),
                        houseWork.getHouseWorkCategory(),
                        houseWork.getAssignedTime()))
                .toList();
    }
}
