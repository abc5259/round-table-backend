package com.roundtable.roundtable.entity.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.global.exception.ScheduleException.CreateScheduleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FrequencyTest {
    private static final String ERROR_MESSAGE = "frequencyType에 맞는 frequencyInterval값이 아닙니다.";

    @DisplayName("Frequency을 만들때 FrequencyInterval을 -1이하로 주면 에러를 던진다.")
    @Test
    void of_fail() {
        //given
        FrequencyType targetType = FrequencyType.ONCE;
        int frequencyInterval = -1;

        //when
        assertThatThrownBy(() -> Frequency.of(targetType, frequencyInterval))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @DisplayName("Frequency을 만들때 FrequencyType이 ONCE 일때는 FrequencyInterval은 0을 주어야한다.")
    @Test
    void of_ONCE() {
        //given
        FrequencyType targetType = FrequencyType.ONCE;
        int frequencyInterval = 0;

        //when
        Frequency frequency = Frequency.of(targetType, frequencyInterval);

        //then
        assertThat(frequency).isNotNull();
     }

    @DisplayName("FrequencyType이 ONCE 일때 FrequencyInterval을 1이상 주면 에러를 던진다.")
    @Test
    void of_ONCE_fail() {
        //given
        FrequencyType targetType = FrequencyType.ONCE;
        int frequencyInterval = 1;

        //when
        assertThatThrownBy(() -> Frequency.of(targetType, frequencyInterval))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @DisplayName("Frequency을 만들때 FrequencyType이 DAILY 일때는 FrequencyInterval은 1이상이어야한다.")
    @Test
    void of_DAILY() {
        //given
        FrequencyType targetType = FrequencyType.DAILY;
        int frequencyInterval = 1;

        //when
        Frequency frequency = Frequency.of(targetType, frequencyInterval);

        //thenÎ
        assertThat(frequency).isNotNull();
    }

    @DisplayName("FrequencyType이 DAILY 일때 FrequencyInterval이 0이하면 에러를 던진다.")
    @Test
    void of_DAILY_fail() {
        //given
        FrequencyType targetType = FrequencyType.DAILY;
        int frequencyInterval = 0;

        //when
        assertThatThrownBy(() -> Frequency.of(targetType, frequencyInterval))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage(ERROR_MESSAGE);
    }

    @DisplayName("Frequency을 만들때 FrequencyType이 WEEKLY 일때는 FrequencyInterval은 1이상 7이하여야한다.")
    @ValueSource(ints = {1,2,3,4,5,6,7})
    @ParameterizedTest
    void of_WEEKLY(int frequencyInterval) {
        //given
        FrequencyType targetType = FrequencyType.WEEKLY;

        //when
        Frequency frequency = Frequency.of(targetType, frequencyInterval);

        //thenÎ
        assertThat(frequency).isNotNull();
    }

    @DisplayName("FrequencyType이 WEEKLY 일때 FrequencyInterval이 0이하, 8이상이면 에러를 던진다.")
    @ValueSource(ints = {0,8})
    @ParameterizedTest
    void of_WEEKLY_fail(int frequencyInterval) {
        //given
        FrequencyType targetType = FrequencyType.WEEKLY;

        //when
        assertThatThrownBy(() -> Frequency.of(targetType, frequencyInterval))
                .isInstanceOf(CreateScheduleException.class)
                .hasMessage(ERROR_MESSAGE);
    }


}