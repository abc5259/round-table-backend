package com.roundtable.roundtable.business.house;

import com.roundtable.roundtable.entity.house.InviteCode;
import com.roundtable.roundtable.global.exception.CoreException.DuplicatedException;
import com.roundtable.roundtable.global.exception.errorcode.HouseErrorCode;
import com.roundtable.roundtable.global.util.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InviteCodeManager {
    public final static int INVITE_CODE_REPEAT_COUNT = 3;
    public static final int INVITE_CODE_LENGTH = 6;

    private final HouseReader houseReader;

    public InviteCode createInviteCode() {
        for (int i = 0; i < INVITE_CODE_REPEAT_COUNT; i++) {
            InviteCode inviteCode = InviteCode.of(RandomStringGenerator.generateRandomString(INVITE_CODE_LENGTH));
            if (!houseReader.isExistInviteCode(inviteCode)) {
                return inviteCode;
            }
        }

        throw new DuplicatedException(HouseErrorCode.DUPLICATED_INVITE_CODE);
    }
}
