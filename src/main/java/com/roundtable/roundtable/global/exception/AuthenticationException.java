package com.roundtable.roundtable.global.exception;

public class AuthenticationException extends ApplicationException {

    public AuthenticationException(String message) {
        super(message);
    }

    public static class JwtAuthenticationException extends AuthenticationException{

        public JwtAuthenticationException(String message) {
            super(message);
        }
    }
}
