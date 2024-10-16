package com.roundtable.roundtable.global.exception.errorcode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScheduleErrorCode implements ErrorCode {

    CATEGORY_NOT_SAME_HOUSE("스케줄과 같은 하우스에 속한 카테고리가 아닙니다."),
    FREQUENCY_NOT_SUPPORT("frequencyType에 맞는 frequencyInterval값이 아닙니다."),
    FREQUENCY_NOT_SUPPORT_WEEKLY("Weekly타입일땐 시작날짜의 요일은 interval로 준 값에 해당하는 요일이어야합니다."),
    INVALID_START_DATE("시작날짜는 과거일 수 없습니다."),
    DUPLICATED_MEMBER_ID("집안일 할당자로 중복된 member는 올 수 없습니다."),
    NOT_FOUND_ID("id에 해당하는 schedule이 없습니다."),
    NOT_ASSIGNED_MANAGER("해당 스케줄을 담당하는 담당자가 아닙니다."),
    ALREADY_COMPLETION_SCHEDULE("이미 완료된 스케줄입니다."),
    NOT_TODAY_SCHEDULE("오늘 수행해야 할 스케줄이 아닙니다.");
    ;

    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getCode() {
        return null;
    }
}
