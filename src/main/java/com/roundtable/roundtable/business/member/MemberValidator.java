package com.roundtable.roundtable.business.member;

import static com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode.*;
import static java.util.Objects.*;

import com.amazonaws.services.kms.model.NotFoundException;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.CoreException.DuplicatedException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.MemberException.InvalidHouseMemberException;
import com.roundtable.roundtable.global.exception.MemberException.MemberAlreadyHasHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNoHouseException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.List;
import java.util.Optional;
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

    public void validateMemberBelongsToHouse(Long memberId) {
        if(memberId == null) {
            throw new NotFoundEntityException(NOT_FOUND);
        }
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND));

        if(!member.isEnterHouse()) {
            throw new MemberNoHouseException();
        }
    }

    public void validateMemberBelongsTo(Long memberId, Long houseId) {
        if(houseId == null) {
            throw new InvalidHouseMemberException();
        }

        if(!memberRepository.existsByIdAndHouse(memberId, House.Id(houseId))) {
            throw new InvalidHouseMemberException();
        }
    }

    public void validateDuplicatedEmail(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new DuplicatedException(DUPLICATED_EMAIL);
        }
    }

    public void validateCanInviteHouse(Member member) {
        if(member.isEnterHouse()) {
            throw new MemberAlreadyHasHouseException();
        }
    }

    public void validateExistMemberId(Long memberId) {
        if(!memberRepository.existsById(memberId)) {
            throw new NotFoundEntityException(MemberErrorCode.NOT_FOUND);
        }
    }
}

