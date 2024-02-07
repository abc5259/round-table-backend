package com.roundtable.roundtable.implement.housework;

import com.roundtable.roundtable.implement.common.BusinessException;

public class DayException extends BusinessException {

    public DayException(String message) {
        super(message);
    }

    public static class DayNotFindException extends DayException {

        public DayNotFindException() {
            super("해당하는 Day를 찾을 수 없습니다.");
        }
    }
}
