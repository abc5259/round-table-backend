package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.member.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.implement.member.MemberException.MemberNotSameHouseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    public void validateMemberInHouse(Member member) {
        if(member.getHouse() == null) {
            throw new MemberNoHouseException();
        }
    }

    public void validateMembersSameHouse(List<Member> members, House house) {
        members.forEach(member -> {
            if(!member.getHouse().getId().equals(house.getId())) {
                throw new MemberNotSameHouseException();
            }
        });
    }
}

