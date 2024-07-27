package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.feedback.Feedback;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NotificationType{
    INVITE(Values.INVITE),
    CHORE_COMPLETE(Values.CHORE_COMPLETE),
    Feedback(Values.FEEDBACK);

    NotificationType(String val) {
        if (!this.name().equals(val)) {
            throw new IllegalArgumentException("Incorrect use of GroupType");
        }
    }

    public static class Values {
        public static final String INVITE = "INVITE";
        public static final String CHORE_COMPLETE = "CHORE_COMPLETE";
        public static final String FEEDBACK = "FEEDBACK";
    }
}
