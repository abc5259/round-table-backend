package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.implement.member.MemberException.MemberNoHouseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    public void validateMemberInHouse(Member member) {
        if(member.getHouse() == null) {
            throw new MemberNoHouseException();
        }
    }
}
