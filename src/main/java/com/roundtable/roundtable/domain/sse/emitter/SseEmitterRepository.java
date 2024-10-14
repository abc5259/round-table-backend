package com.roundtable.roundtable.domain.sse.emitter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@Component
public class SseEmitterRepository {

    private static final String COMPLETE = "COMPLETE";
    private static final String TIME_OUT = "TIME_OUT";
    private static final String ERROR = "ERROR";

    private final Map<SseEmitterId, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitters save(final SseEmitterId emitterId, final SseEmitter sseEmitter) {
        addEmitterHandlers(emitterId, sseEmitter);
        emitters.put(emitterId, sseEmitter);

        log.info("SseEmitter 저장 {}", emitterId.toString());
        return new SseEmitters(Map.of(emitterId, sseEmitter));
    }

    private void addEmitterHandlers(final SseEmitterId emitterId, final SseEmitter sseEmitter) {
        sseEmitter.onCompletion(() -> removeEmitter(emitterId, COMPLETE));
        sseEmitter.onTimeout(() -> removeEmitter(emitterId, TIME_OUT));
        sseEmitter.onError((e) -> {
            log.error("SseEmitter 에러 : {}", emitterId.toString(), e);
            removeEmitter(emitterId, ERROR);
        });
    }

    private void removeEmitter(final SseEmitterId emitterId, final String cause) {
        if (emitters.remove(emitterId) != null) {
            log.info("SseEmitter 제거 : {}, {}", emitterId.toString(), cause);
        } else {
            log.warn("SseEmitter 제거 실패 - EmitterId 찾을 수 없음 : {}", emitterId.toString());
        }
    }

    public SseEmitters findByHouseIdAndMemberId(final Long houseId, List<Long> memberIds) {
        Map<SseEmitterId, SseEmitter> filteredEmitters = this.emitters.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isHouseId(houseId) && entry.getKey().containMemberId(memberIds))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        log.info("SseEmitter 탐색 {}개, houseId : {}, memberIds: {}", filteredEmitters.size(), houseId, memberIds);
        return new SseEmitters(filteredEmitters);
    }
}
