package com.roundtable.roundtable.domain.schedulecomment;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleCommentErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentTest {

    @DisplayName("Content의 길이는 3000자를 초과할 수 없다.")
    @Test
    void of_fail() {
        //given
        String content = "x".repeat(3001);

        //when //then
        assertThatThrownBy(() -> Content.of(content))
                .isInstanceOf(CreateEntityException.class)
                .hasMessage(ScheduleCommentErrorCode.INVALID_CONTENT_LENGTH.getMessage());

     }

    @DisplayName("Content의 길이는 3000자이하면 생성이 가능하다.")
    @Test
    void of() {
        //given
        String contentStr = "x".repeat(3000);

        //when
        Content content = Content.of(contentStr);

        //then
        assertThat(content).isNotNull()
                .extracting(Content::getContent)
                .isEqualTo(contentStr);
    }
}