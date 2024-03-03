package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode;

public class AuthenticationException extends CoreException {

    public AuthenticationException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public static class JwtAuthenticationException extends AuthenticationException{

        public JwtAuthenticationException(AuthErrorCode errorCode) {
            super(errorCode);
        }
    }
}
