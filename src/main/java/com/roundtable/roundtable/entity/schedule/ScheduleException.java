package com.roundtable.roundtable.entity.schedule;

import com.roundtable.roundtable.implement.common.BusinessException;

public class ScheduleException extends BusinessException {
    public ScheduleException(String message) {
        super(message);
    }

    public static class CreateScheduleException extends ScheduleException {

        public CreateScheduleException(String message) {
            super(message);
        }
    }
}
