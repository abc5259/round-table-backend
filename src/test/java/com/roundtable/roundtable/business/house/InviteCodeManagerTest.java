package com.roundtable.roundtable.business.house;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.global.exception.CoreException.DuplicatedException;
import com.roundtable.roundtable.global.exception.errorcode.HouseErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class InviteCodeManagerTest extends IntegrationTestSupport {

    HouseReader houseReader = mock(HouseReader.class);
    InviteCodeManager inviteCodeManager = new InviteCodeManager(houseReader);

    @DisplayName("중복된 코드가 없다면 초대 코드를 바로 생성한다.")
    @Test
    public void testCreateInviteCode_NoDuplicates() {
        // houseReader.isExistInviteCode()가 false를 반환하도록 설정
        when(houseReader.isExistInviteCode(any())).thenReturn(false);

        // 초대 코드 생성
        InviteCode inviteCode = inviteCodeManager.createInviteCode();

        // 생성된 초대 코드가 null이 아닌지 확인
        assertNotNull(inviteCode);
    }

    @DisplayName("중복된 코드가 있더라도 최대 3번까지 재시도한다.")
    @Test
    public void testCreateInviteCode_WithDuplicates() {
        // 초대 코드가 이미 존재하는 경우를 가정하여 설정
        when(houseReader.isExistInviteCode(any())).thenReturn(true, true, false);

        // 초대 코드 생성
        InviteCode inviteCode = inviteCodeManager.createInviteCode();

        // 생성된 초대 코드가 null이 아닌지 확인
        assertNotNull(inviteCode);
    }

    @DisplayName("중복된 코드가 3번 연속 발생하면 에러를 던진다.")
    @Test
    public void testCreateInviteCode_AllDuplicates() {
        // 초대 코드가 모두 중복되는 경우를 가정하여 설정
        when(houseReader.isExistInviteCode(any())).thenReturn(true);

        // 예외 발생 여부 확인
        assertThatThrownBy( () -> inviteCodeManager.createInviteCode())
                .isInstanceOf(DuplicatedException.class)
                .hasMessage(HouseErrorCode.DUPLICATED_INVITE_CODE.getMessage());
    }


}