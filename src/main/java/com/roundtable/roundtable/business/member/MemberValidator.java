package com.roundtable.roundtable.business.member;

import static java.util.Objects.*;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.CoreException.DuplicatedException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateMembersSameHouse(List<Member> members, House house) {
        members.forEach(member -> {
            validateMemberInHouse(member);
            if(!member.isSameHouse(house)) {
                throw new MemberNotSameHouseException();
            }
        });
    }

    private void validateMemberInHouse(Member member) {
        if(member.getHouse() == null) {
            throw new MemberNoHouseException();
        }
    }

    public void validateMemberBelongsToHouse(Long memberId, Long houseId) {
        if(isNull(houseId)) {
            throw new MemberNoHouseException();
        }

        if(!memberRepository.existsByIdAndHouse(memberId, House.Id(houseId))) {
            throw new MemberNotSameHouseException();
        }
    }

    public void validateDuplicatedEmail(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new DuplicatedException(MemberErrorCode.DUPLICATED_EMAIL);
        }
    }
}

