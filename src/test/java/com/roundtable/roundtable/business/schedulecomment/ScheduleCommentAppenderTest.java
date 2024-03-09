package com.roundtable.roundtable.business.schedulecomment;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.entity.category.Category;
import com.roundtable.roundtable.entity.category.CategoryRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.house.HouseRepository;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.Frequency;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.entity.schedule.Schedule;
import com.roundtable.roundtable.entity.schedule.ScheduleRepository;
import com.roundtable.roundtable.entity.schedulecomment.ScheduleComment;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @DisplayName("ScheduleComment를 ScheduleComment 테이블에 추가한다.")
    @Test
    void append() {
        //given
        House house = appendHouse();
        Member member = appendMember(house);
        Category category = appendCategory(house);
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
        House house = House.builder().name("house").build();
        houseRepository.save(house);
        return house;
    }

    private Schedule appendSchedule(Category category, House house) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(category)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .frequency(Frequency.builder().frequencyType(FrequencyType.DAILY).frequencyInterval(2).build())
                .sequence(1)
                .sequenceSize(1)
                .house(house)
                .divisionType(DivisionType.FIX)
                .build();
        return scheduleRepository.save(schedule);
    }

    private Category appendCategory(House house) {
        Category category = Category.builder().house(house).name("name").point(1).build();
        return categoryRepository.save(category);
    }
}