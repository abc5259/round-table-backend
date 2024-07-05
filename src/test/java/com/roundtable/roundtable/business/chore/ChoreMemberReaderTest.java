package com.roundtable.roundtable.business.chore;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMemberRepository;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChoreMemberReaderTest {

    @Mock
    private ChoreMemberRepository choreMemberRepository;

    @InjectMocks
    private ChoreMemberReader choreMemberReader;


    @DisplayName("memberId와 choreId를 이용해 ChoreMember가 존재하는지 확인할 수 있다.")
    @Test
    void existByMemberIdAndChoreId() {
        //given
        boolean expected = true;
        when(choreMemberRepository.existsChoreMemberByMemberAndChore(any(Member.class), any(Chore.class))).thenReturn(expected);

        //when
        boolean result = choreMemberReader.existByMemberIdAndChoreId(1L, 1L);

        //then
        assertThat(result).isEqualTo(expected);

    }
}