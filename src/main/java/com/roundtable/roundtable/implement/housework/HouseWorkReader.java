package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.entity.housework.HouseWorkRepository;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.member.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HouseWorkReader {

    private final MemberValidator memberValidator;
    private final HouseWorkRepository houseWorkRepository;
    public void findAllHouseWork(Member member) {
        memberValidator.validateMemberInHouse(member);

        /**
         * select * from House_Work_member as hwm
         *          inner join hwm.house_id = {현재 하우스 id}
         *          inner join hwm.house_work_id = house_work_id.id
         *          inner join house_work_id.id = 일회성.id and 일회상.assigned_time > {현재}
         *          inner join house_work_id.id = 일회성.id and 일회상.assigned_time > {현재}
         */

    }
}
