package com.roundtable.roundtable.global.exception;

import static com.roundtable.roundtable.global.exception.errorcode.ChoreErrorCode.*;

import com.roundtable.roundtable.global.exception.errorcode.ChoreErrorCode;

public class ChoreException extends CoreException {

    public ChoreException(ChoreErrorCode errorCode) {
        super(errorCode);
    }

    public static class AlreadyCompletedException extends ChoreException {
        public AlreadyCompletedException() {
            super(ALREADY_COMPLETED_CHORE);
        }
    }

    public static class NotCompletedException extends ChoreException {
        public NotCompletedException() {
            super(NOT_COMPLETED_CHORE);
        }
    }
}
