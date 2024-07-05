package com.roundtable.roundtable.business.chore.unit;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMemberRepository;
import com.roundtable.roundtable.domain.chore.ChoreRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChoreReaderUnitTest {

    @Mock
    private ChoreRepository choreRepository;

    @Mock
    private ChoreMemberRepository choreMemberRepository;

    @InjectMocks
    private ChoreReader choreReader;

    @DisplayName("id를 이용하여 Chore을 찾을 수 있다.")
    @Test
    void readById() {
        //given
        Long choreId = 1L;
        Chore expected = Chore.builder().id(1L).build();
        when(choreRepository.findById(choreId)).thenReturn(Optional.ofNullable(expected));

        //when
        Chore result = choreReader.readById(choreId);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("id를 이용하여 Chore을 찾을 수 있다면 에러를 던진다")
    @Test
    void readByIdWithNotFound() {
        //given
        Long choreId = 1L;
        when(choreRepository.findById(choreId)).thenReturn(Optional.empty());

        //when //then
        assertThatThrownBy(() -> choreReader.readById(choreId))
                .isInstanceOf(NotFoundEntityException.class);
    }
}
