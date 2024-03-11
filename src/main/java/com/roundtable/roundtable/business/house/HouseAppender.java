package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.global.exception.CoreException.DuplicatedException;
import com.roundtable.roundtable.global.exception.errorcode.HouseErrorCode;
import com.roundtable.roundtable.global.util.RandomStringGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class HouseAppender {

    public final static int INVITE_CODE_REPEAT_COUNT = 3;

    private final HouseReader houseReader;

    private final HouseRepository houseRepository;

    public void appendHouse(CreateHouse createHouse, Member houseOwner) {
        House house = House.of(createHouse.name(), createInviteCode());
        houseRepository.save(house);

        houseOwner.enterHouse(house);
    }

    private InviteCode createInviteCode() {
        for (int i = 0; i < INVITE_CODE_REPEAT_COUNT; i++) {
            InviteCode inviteCode = InviteCode.of(RandomStringGenerator.generateRandomString());
            if (!houseReader.isExistInviteCode(inviteCode)) {
                return inviteCode;
            }
        }

        throw new DuplicatedException(HouseErrorCode.DUPLICATED_INVITE_CODE);
    }
}