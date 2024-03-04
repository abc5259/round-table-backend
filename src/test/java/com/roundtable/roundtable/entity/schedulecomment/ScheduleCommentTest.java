package com.roundtable.roundtable.entity.schedulecomment;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleCommentErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleCommentTest {

    @DisplayName("스케줄 댓글을 생성할 수 있다.")
    @Test
    void create() {
        //given
        House house = House.builder().id(1L).name("house").build();
        Member writer = Member.builder().name("member").house(house).build();
        Schedule schedule = Schedule.builder().house(house).build();
        Content content = Content.builder().content("content").build();

        //when
        ScheduleComment scheduleComment = ScheduleComment.create(content, schedule, writer);

        //then
        assertThat(scheduleComment).isNotNull()
                .extracting("writer", "schedule", "content")
                .containsExactly(writer, schedule, content);

     }

    @DisplayName("writer과 schedule이 서로 다른 house에 존재한다면 댓글을 작성할 수 없다.")
    @Test
    void create_fail() {
        //given
        House house1 = House.builder().id(1L).name("house1").build();
        House house2 = House.builder().id(2L).name("house2").build();
        Member writer = Member.builder().name("member").house(house1).build();
        Schedule schedule = Schedule.builder().house(house2).build();
        Content content = Content.builder().content("content").build();

        //when
        assertThatThrownBy(() -> ScheduleComment.create(content, schedule, writer))
                .isInstanceOf(CreateEntityException.class)
                .hasMessage(ScheduleCommentErrorCode.NOT_SAME_HOUSE.getMessage());

    }


}