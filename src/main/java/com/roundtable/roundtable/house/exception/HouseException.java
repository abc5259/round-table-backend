package com.roundtable.roundtable.house.exception;

import com.roundtable.roundtable.global.exception.BusinessException;
import com.roundtable.roundtable.house.domain.House;

public class HouseException extends BusinessException {

    public HouseException(String message) {
        super(message);
    }

    public static class HouseMemberMaxException extends HouseException {

        public HouseMemberMaxException() {
            super("하우스의 최대 인원은 " + House.MAX_MEMBER_SIZE + "명 입니다.");
        }
    }

    public static class HouseNotFoundException extends HouseException {

        public HouseNotFoundException() {
            super("존재하지 않는 하우스입니다.");
        }
    }
}
