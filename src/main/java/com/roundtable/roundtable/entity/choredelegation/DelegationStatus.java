package com.roundtable.roundtable.entity.choredelegation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DelegationStatus {
    APPROVED("승인"), REJECTED("거절");

    private final String description;
}
