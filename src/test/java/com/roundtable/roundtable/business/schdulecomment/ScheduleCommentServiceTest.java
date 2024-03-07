package com.roundtable.roundtable.business.schdulecomment;

import static org.assertj.core.api.Assertions.*;

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
import com.roundtable.roundtable.entity.schedulecomment.ScheduleCommentRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ScheduleCommentServiceTest {
    @Autowired
    private ScheduleCommentService scheduleCommentService;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ScheduleCommentRepository scheduleCommentRepository;

    @DisplayName("ScheduleComment를 생성할 수 있다.")
    @Test
    void createScheduleComment() {
        //given
        House house = appendHouse();
        Member member = appendMember(house);
        Category category = appendCategory(house);
        Schedule schedule = appendSchedule(category, house);
        String contentStr = "content";

        CreateScheduleCommentDto createScheduleCommentDto = new CreateScheduleCommentDto(contentStr, member.getId(),
                schedule.getId());

        //when
        Long scheduleCommentId = scheduleCommentService.createScheduleComment(createScheduleCommentDto);

        //then
        assertThat(scheduleCommentId).isNotNull();
        ScheduleComment scheduleComment = scheduleCommentRepository.findById(scheduleCommentId).orElseThrow();
        assertThat(scheduleComment).isNotNull()
                .extracting("writer", "schedule")
                .contains(member, schedule);
        assertThat(scheduleComment.getContent().getContent()).isEqualTo(contentStr);
    }

    @DisplayName("member가 존재하지 않는다면 ScheduleComment를 생성할 수 없다.")
    @Test
    void createScheduleComment_notFoundMember() {
        //given
        House house = appendHouse();
        Category category = appendCategory(house);
        Schedule schedule = appendSchedule(category, house);
        String contentStr = "content";

        CreateScheduleCommentDto createScheduleCommentDto = new CreateScheduleCommentDto(contentStr, 1L,
                schedule.getId());

        //when
        assertThatThrownBy(() -> scheduleCommentService.createScheduleComment(createScheduleCommentDto))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(MemberErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("schedule이 존재하지 않는다면 ScheduleComment를 생성할 수 없다.")
    @Test
    void createScheduleComment_notFoundSchedule() {
        //given
        House house = appendHouse();
        Category category = appendCategory(house);
        Member member = appendMember(house);
        String contentStr = "content";

        CreateScheduleCommentDto createScheduleCommentDto = new CreateScheduleCommentDto(contentStr, member.getId(), 1L);

        //when
        assertThatThrownBy(() -> scheduleCommentService.createScheduleComment(createScheduleCommentDto))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(ScheduleErrorCode.NOT_FOUND_ID.getMessage());
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