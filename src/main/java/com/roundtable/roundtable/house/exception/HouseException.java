package com.roundtable.roundtable.house.exception;

import com.roundtable.roundtable.house.domain.House;

public class HouseException extends RuntimeException {

    public HouseException(String message) {
        super(message);
    }

    public static class HouseMemberMaxException extends HouseException {

        public HouseMemberMaxException() {
            super("하우스의 최대 인원은 " + House.MAX_MEMBER_SIZE + "명 입니다.");
        }
    }
}
