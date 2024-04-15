package com.roundtable.roundtable.business.schedulecomment.dto;

import com.roundtable.roundtable.business.common.AuthMember;

/**
 *  서비스 레이어에서 사용중인 dto
 */
public record CreateScheduleCommentDto(
        String content,
        Long scheduleId
) {
}
