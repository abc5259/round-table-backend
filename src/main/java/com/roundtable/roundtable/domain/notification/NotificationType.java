package com.roundtable.roundtable.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationType{
    INVITE(Values.INVITE);

    NotificationType(String val) {
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Incorrect use of GroupType");
        }
    }

    public static class Values {
        public static final String INVITE = "INVITE";
    }
}
