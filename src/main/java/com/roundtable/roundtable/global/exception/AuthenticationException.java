package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;

public class AuthenticationException extends CoreException {

    public AuthenticationException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthenticationException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public static class JwtAuthenticationException extends AuthenticationException{

        public JwtAuthenticationException(AuthErrorCode errorCode) {
            super(errorCode);
        }

        public JwtAuthenticationException(ErrorCode errorCode, Throwable cause) {
            super(errorCode, cause);
        }
    }
}
