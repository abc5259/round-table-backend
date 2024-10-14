package com.roundtable.roundtable.business.sse;

import com.roundtable.roundtable.domain.sse.emitter.SseEmitterRepository;
import com.roundtable.roundtable.domain.sse.emitter.SseEmitters;
import com.roundtable.roundtable.domain.sse.event.SseEvent;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HouseSsePublisher {

    private final SseEmitterRepository sseEmitterRepository;

    public void send(Long houseId, List<Long> receiverIds, SseEvent sseEvent) {
        SseEmitters sseEmitters = sseEmitterRepository.findByHouseIdAndMemberId(houseId, receiverIds);
        sseEmitters.sendEvent(sseEvent, LocalDateTime.now());
    }
}
