package com.roundtable.roundtable.domain.sse.event;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SseEventId {
    private static final String DELIMITER = "_";

    private final Long houseId;
    private final Long memberId;
    private final LocalDateTime timestamp;

    private SseEventId(Long houseId, Long memberId, LocalDateTime timestamp) {
        this.houseId = houseId;
        this.memberId = memberId;
        this.timestamp = timestamp;
    }

    public static SseEventId of(Long houseId, Long memberId, LocalDateTime timestamp) {
        return new SseEventId(houseId, memberId, timestamp);
    }

    @Override
    public String toString() {
        return houseId + DELIMITER + memberId + DELIMITER + timestamp;
    }
}
