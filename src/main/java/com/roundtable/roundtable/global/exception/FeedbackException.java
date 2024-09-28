package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.FeedbackErrorCode;

public class FeedbackException extends CoreException {
    public FeedbackException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public FeedbackException(FeedbackErrorCode errorCode) {
        super(errorCode);
    }
}
