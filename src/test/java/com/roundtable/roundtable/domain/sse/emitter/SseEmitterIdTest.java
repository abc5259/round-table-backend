package com.roundtable.roundtable.domain.sse.emitter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SseEmitterIdTest {

    @DisplayName("SseEmitterId를 만들 수 있다.")
    @Test
    void CreateSseEmitterId() {
        //given
        Long houseId = 1L;
        Long memberId = 2L;
        LocalDateTime now = LocalDateTime.now();

        //when
        SseEmitterId sseEmitterId = SseEmitterId.of(houseId, memberId, now);

        //then
        Assertions.assertThat(sseEmitterId).extracting("houseId", "memberId", "timestamp")
                .contains(1L, 2L, now);
    }

    @DisplayName("SseEmitterId 생성시 houseId값이 null인 경우 -1로 만든다.")
    @Test
    void CreateSseEmitterIdWithNonHouseId() {
        //given
        Long houseId = null;
        Long memberId = 2L;
        LocalDateTime now = LocalDateTime.now();

        //when
        SseEmitterId sseEmitterId = SseEmitterId.of(houseId, memberId, now);

        //then
        Assertions.assertThat(sseEmitterId).extracting("houseId", "memberId", "timestamp")
                .contains(-1L, 2L, now);
    }
}