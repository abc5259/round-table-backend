package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;

public class ScheduleException extends CoreException {
    public ScheduleException(ScheduleErrorCode errorCode) {
        super(errorCode);
    }
}
