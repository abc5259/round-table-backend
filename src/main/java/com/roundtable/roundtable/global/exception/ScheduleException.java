package com.roundtable.roundtable.global.exception;

public class ScheduleException extends RuntimeException {
    public ScheduleException(String message) {
        super(message);
    }

    public static class CreateScheduleException extends ScheduleException {

        public CreateScheduleException(String message) {
            super(message);
        }
    }
}
