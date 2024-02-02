package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.implement.common.BusinessException;

public class MemberException extends BusinessException {
    public MemberException(String message) {
        super(message);
    }

    public static class EmailDuplicatedException extends MemberException {

        public EmailDuplicatedException(String email) {
            super(String.format("해당 이메일이 이미 존재합니다. email: %s", email));
        }
    }

    public static class MemberNotFoundException extends MemberException {

        public MemberNotFoundException() {
            super("해당 유저가 존재하지 않습니다.");
        }
    }

    public static class MemberUnAuthorizationException extends MemberException {

        public MemberUnAuthorizationException(String message) {
            super(message);
        }
    }

    public static class MemberAlreadyHasHouseException extends MemberException {

        public MemberAlreadyHasHouseException() {
            super("참여중인 하우스가 있습니다.");
        }
    }
}
