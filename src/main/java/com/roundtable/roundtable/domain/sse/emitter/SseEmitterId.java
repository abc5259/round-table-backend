package com.roundtable.roundtable.domain.sse.emitter;

import com.roundtable.roundtable.domain.sse.event.SseEventId;
import java.time.LocalDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SseEmitterId {

    private static final String DELIMITER = "_";
    private static final Long EMPTY_HOUSE = -1L;

    private final Long houseId;
    private final Long memberId;
    private final LocalDateTime timestamp;

    private SseEmitterId(Long houseId, Long memberId, LocalDateTime timestamp) {
        this.houseId = houseId;
        this.memberId = memberId;
        this.timestamp = timestamp;
    }

    public static SseEmitterId of(Long houseId, Long memberId, LocalDateTime timestamp) {
        if(houseId == null) {
            houseId = EMPTY_HOUSE;
        }
        return new SseEmitterId(houseId, memberId, timestamp);
    }

    public SseEventId toSseEventId(LocalDateTime timestamp) {
        return SseEventId.of(this.houseId, this.memberId, timestamp);
    }

    @Override
    public String toString() {
        return houseId + DELIMITER + memberId + DELIMITER + timestamp;
    }

    public boolean isHouseId(Long houseId) {
        return this.houseId.equals(houseId);
    }

    public boolean containMemberId(List<Long> memberIds) {
        return memberIds.contains(memberId);
    }

    public boolean isNonHouse() {
        return houseId.equals(EMPTY_HOUSE);
    }
}
