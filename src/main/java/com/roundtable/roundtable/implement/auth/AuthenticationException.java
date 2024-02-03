package com.roundtable.roundtable.implement.auth;

import com.roundtable.roundtable.implement.common.BusinessException;

public class AuthenticationException extends BusinessException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class JwtAuthenticationException extends AuthenticationException{

        public JwtAuthenticationException(String message) {
            super(message);
        }
    }
}
