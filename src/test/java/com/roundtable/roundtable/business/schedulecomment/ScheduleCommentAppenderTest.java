package com.roundtable.roundtable.business.schedulecomment;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.schedulecomment.dto.CreateScheduleComment;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleComment;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleCommentAppenderTest extends IntegrationTestSupport {

    @Autowired
    private ScheduleCommentAppender scheduleCommentAppender;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;


    @DisplayName("ScheduleComment를 ScheduleComment 테이블에 추가한다.")
    @Test
    void append() {
        //given
        House house = appendHouse();
        Member member = appendMember(house);
        Category category = Category.CLEANING;
        Schedule schedule = appendSchedule(category, house);

        String contentStr = "content";
        CreateScheduleComment createScheduleComment = new CreateScheduleComment(member, schedule, contentStr);

        //when
        ScheduleComment scheduleComment = scheduleCommentAppender.append(createScheduleComment);

        //then
        assertThat(scheduleComment).isNotNull()
                .extracting("schedule", "writer")
                .containsExactly(
                        schedule,
                        member);

        assertThat(scheduleComment.getId()).isNotNull();
        assertThat(scheduleComment.getContent().getContent()).isEqualTo(contentStr);
     }

    private Member appendMember(House house) {
        Member member = Member.builder().email("email").password("password").house(house).build();
        return memberRepository.save(member);
    }

    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(Category category, House house) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(category)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .build();
        return scheduleRepository.save(schedule);
    }
}