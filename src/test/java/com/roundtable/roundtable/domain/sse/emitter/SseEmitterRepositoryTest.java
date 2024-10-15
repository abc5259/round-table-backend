package com.roundtable.roundtable.domain.sse.emitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class SseEmitterRepositoryTest {

    @DisplayName("SseEmitter를 저장한다.")
    @Test
    void save() {
        //given
        SseEmitterRepository sseEmitterRepository = new SseEmitterRepository();
        SseEmitterId sseEmitterId = SseEmitterId.of(1L, 2L, LocalDateTime.now());
        SseEmitter sseEmitter = mock(SseEmitter.class);

        //when
        SseEmitters sseEmitters = sseEmitterRepository.save(sseEmitterId, sseEmitter);

        //then
        verify(sseEmitter, times(1)).onCompletion(any());
        verify(sseEmitter, times(1)).onTimeout(any());
        verify(sseEmitter, times(1)).onError(any());

        Map<SseEmitterId, SseEmitter> emitters = sseEmitters.getEmitters();
        assertThat(emitters).hasSize(1);
        assertThat(emitters.get(sseEmitterId)).isEqualTo(sseEmitter);

        Map<SseEmitterId, SseEmitter> repositoryEmitters = sseEmitterRepository.getEmitters();
        assertThat(repositoryEmitters).hasSize(1);
        assertThat(repositoryEmitters.get(sseEmitterId)).isEqualTo(sseEmitter);
    }

    @DisplayName("하우스 id와 member id를 이용해 저장된 SseEmitter를 조회한다.")
    @Test
    void findByHouseIdAndMemberId() {
        //given
        SseEmitterRepository sseEmitterRepository = new SseEmitterRepository();
        SseEmitterId sseEmitterId1 = SseEmitterId.of(1L, 2L, LocalDateTime.now());
        SseEmitterId sseEmitterId2 = SseEmitterId.of(1L, 3L, LocalDateTime.now());
        SseEmitterId sseEmitterId3 = SseEmitterId.of(1L, 4L, LocalDateTime.now());
        SseEmitterId sseEmitterId4 = SseEmitterId.of(2L, 1L, LocalDateTime.now());

        sseEmitterRepository.save(sseEmitterId1, mock(SseEmitter.class));
        sseEmitterRepository.save(sseEmitterId2, mock(SseEmitter.class));
        sseEmitterRepository.save(sseEmitterId3, mock(SseEmitter.class));
        sseEmitterRepository.save(sseEmitterId4, mock(SseEmitter.class));

        //when
        SseEmitters sseEmitters = sseEmitterRepository.findByHouseIdAndMemberId(1L, List.of(1L, 2L, 3L));

        //then
        Map<SseEmitterId, SseEmitter> emitters = sseEmitters.getEmitters();
        assertThat(emitters).hasSize(2);
        assertThat(emitters.get(sseEmitterId1)).isNotNull();
        assertThat(emitters.get(sseEmitterId2)).isNotNull();
    }

    @DisplayName("하우스가 없는 member id를 이용해 저장된 SseEmitter를 조회한다.")
    @Test
    void finByMemberIdAndNonHouse() {
        //given
        SseEmitterRepository sseEmitterRepository = new SseEmitterRepository();
        SseEmitterId sseEmitterId1 = SseEmitterId.of(1L, 2L, LocalDateTime.now());
        SseEmitterId sseEmitterId2 = SseEmitterId.of(null, 3L, LocalDateTime.now());
        SseEmitterId sseEmitterId3 = SseEmitterId.of(1L, 4L, LocalDateTime.now());
        SseEmitterId sseEmitterId4 = SseEmitterId.of(null, 1L, LocalDateTime.now());

        sseEmitterRepository.save(sseEmitterId1, mock(SseEmitter.class));
        sseEmitterRepository.save(sseEmitterId2, mock(SseEmitter.class));
        sseEmitterRepository.save(sseEmitterId3, mock(SseEmitter.class));
        sseEmitterRepository.save(sseEmitterId4, mock(SseEmitter.class));

        //when
        SseEmitters sseEmitters = sseEmitterRepository.finByMemberIdAndNonHouse(List.of(1L, 2L, 3L));

        //then
        Map<SseEmitterId, SseEmitter> emitters = sseEmitters.getEmitters();
        assertThat(emitters).hasSize(2);
        assertThat(emitters.get(sseEmitterId2)).isNotNull();
        assertThat(emitters.get(sseEmitterId4)).isNotNull();
    }

}