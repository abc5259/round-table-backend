package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.ChoreErrorCode;

public class ChoreException extends CoreException {

    public ChoreException(ChoreErrorCode errorCode) {
        super(errorCode);
    }

    public static class AlreadyCompletedException extends ChoreException {
        public AlreadyCompletedException() {
            super(ChoreErrorCode.ALREADY_COMPLETED_CHORE);
        }
    }
}
