package com.roundtable.roundtable.domain.notification;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationType{
    INVITE(Values.INVITE),
    CHORE_COMPLETE(Values.CHORE_COMPLETE),
    SCHEDULE_COMPLETE(Values.SCHEDULE_COMPLETION),
    FEEDBACK(Values.FEEDBACK);

    NotificationType(String val) {
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Incorrect use of GroupType");
        }
    }

    public static class Values {
        public static final String INVITE = "INVITE";
        public static final String CHORE_COMPLETE = "CHORE_COMPLETE";
        public static final String FEEDBACK = "FEEDBACK";
        public static final String SCHEDULE_COMPLETION = "SCHEDULE_COMPLETION";
    }
}
