package com.roundtable.roundtable.member.exception;

public class MemberException extends RuntimeException {
    public MemberException(String message) {
        super(message);
    }

    public static class EmailDuplicatedException extends MemberException {

        public EmailDuplicatedException(String email) {
            super(String.format("해당 이메일이 이미 존재합니다. email: %s", email));
        }
    }
}
