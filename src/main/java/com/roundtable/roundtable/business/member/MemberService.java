package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.business.member.dto.MemberProfile;
import com.roundtable.roundtable.business.member.dto.response.HouseDetailResponse;
import com.roundtable.roundtable.business.member.dto.response.MemberDetailResponse;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class  MemberService {

    private final MemberReader memberReader;
    private final MemberUpdater memberUpdater;
    private final MemberValidator memberValidator;

    public void settingProfile(final Long memberId, final MemberProfile memberProfile) {
        memberUpdater.settingProfile(memberId, memberProfile);
    }

    public boolean isExistEmail(String email) {
        return memberReader.existEmail(email);
    }

    public boolean canInviteHouse(final Long memberId) {
        Member member = memberReader.findById(memberId);
        return !member.isEnterHouse();
    }

    public MemberDetailResponse findMemberDetail(final Long memberId) {

        Member member = memberReader.findById(memberId);

        return new MemberDetailResponse(
                member.getId(),
                member.getName(),
                member.getGender(),
                createHouseDetail(member)
        );
    }

    private HouseDetailResponse createHouseDetail(Member member) {
        if (member.isEnterHouse()) {
            House house = member.getHouse();
            return new HouseDetailResponse(
                    house.getId(),
                    house.getName()
            );
        }
        return null;
    }

    public void validateCanInviteHouse(String email) {
        memberValidator.validateCanInviteHouse(email);
    }
}
