package com.roundtable.roundtable.domain.sse.emitter;

import static org.mockito.BDDMockito.*;

import com.roundtable.roundtable.domain.sse.TestSseEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

class SseEmittersTest {

    @DisplayName("이벤트를 전송할 수 있다.")
    @Test
    void sendEvent() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Map<SseEmitterId, SseEmitter> sseEmitterMap = Map.of(
                SseEmitterId.of(1L, 2L, now), mock(SseEmitter.class),
                SseEmitterId.of(1L, 3L, now), mock(SseEmitter.class),
                SseEmitterId.of(1L, 4L, now), mock(SseEmitter.class)
        );
        SseEmitters sseEmitters = new SseEmitters(sseEmitterMap);
        TestSseEvent sseEvent = new TestSseEvent();

        //when
        sseEmitters.sendEvent(sseEvent, now);

        //then
        sseEmitterMap.forEach((emitterId, emitter) -> {
            try {
                verify(emitter, times(1)).send(any(Set.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}