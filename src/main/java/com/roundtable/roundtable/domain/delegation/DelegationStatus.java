package com.roundtable.roundtable.domain.delegation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DelegationStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절");

    private final String description;
}
