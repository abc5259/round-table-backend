package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.implement.common.BusinessException;

public class DayException extends BusinessException {

    public DayException(String message) {
        super(message);
    }

    public static class DayNotFoundException extends DayException {

        public DayNotFoundException() {
            super("해당하는 Day를 찾을 수 없습니다.");
        }
    }
}
