package com.roundtable.roundtable.global.exception;

import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;

public class MemberException extends CoreException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }

    public static class MemberUnAuthorizationException extends MemberException {

        public MemberUnAuthorizationException(MemberErrorCode errorCode) {
            super(errorCode);
        }
    }

    public static class MemberAlreadyHasHouseException extends MemberException {

        public MemberAlreadyHasHouseException() {
            super(MemberErrorCode.ALREADY_HAS_HOUSE);
        }
    }

    public static class MemberNoHouseException extends MemberException {

        public MemberNoHouseException() {
            super(MemberErrorCode.NO_HAS_HOUSE);
        }
    }

    public static class MemberNotSameHouseException extends MemberException {

        public MemberNotSameHouseException() {
            super(MemberErrorCode.NOT_SAME_HOUSE);
        }
    }
}
