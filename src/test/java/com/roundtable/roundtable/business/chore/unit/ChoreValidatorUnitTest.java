package com.roundtable.roundtable.business.chore.unit;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.*;

import com.roundtable.roundtable.business.chore.ChoreMemberReader;
import com.roundtable.roundtable.business.chore.ChoreValidator;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.global.exception.ChoreException.AlreadyCompletedException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChoreValidatorUnitTest {

    @Mock
    private ChoreMemberReader choreMemberReader;

    @InjectMocks
    private ChoreValidator choreValidator;

    @DisplayName("집안일을 담당하는 멤버가 아니라면 에러를 던진다.")
    @Test
    void validateChoreAssignedToMember() {
        //given
        given(choreMemberReader.existByMemberIdAndChoreId(1L, 1L)).willReturn(false);

        //when //then
        assertThatThrownBy(() -> choreValidator.validateChoreAssignedToMember(1L, 1L))
                .isInstanceOf(NotFoundEntityException.class);

    }

    @DisplayName("집안일을 담당하는 멤버라면 에러를 던지지 않는다.")
    @Test
    void validateChoreAssignedToMember_noThrow() {
        //given
        given(choreMemberReader.existByMemberIdAndChoreId(1L, 1L)).willReturn(true);

        //when //then
        assertDoesNotThrow(() -> choreValidator.validateChoreAssignedToMember(1L, 1L));
    }

    @DisplayName("집안일을 완료했으면 에러를 던진다.")
    @Test
    void validateCompleteChore() {
        //given
        Chore chore = Chore.builder().isCompleted(true).build();

        //when //then
        assertThatThrownBy(() -> choreValidator.validateCompleteChore(chore))
                .isInstanceOf(AlreadyCompletedException.class);

    }

    @DisplayName("집안일을 완료하지 않았으면 에러를 던지지 않는다.")
    @Test
    void validateCompleteChore_notThrow() {
        //given
        Chore chore = Chore.builder().isCompleted(false).build();

        //when //then
        assertDoesNotThrow(() -> choreValidator.validateCompleteChore(chore));
    }
}
