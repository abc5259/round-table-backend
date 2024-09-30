package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.DelegationErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;

public class DelegationException extends CoreException {
    public DelegationException(DelegationErrorCode errorCode) {
        super(errorCode);
    }

    public DelegationException(DelegationErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
