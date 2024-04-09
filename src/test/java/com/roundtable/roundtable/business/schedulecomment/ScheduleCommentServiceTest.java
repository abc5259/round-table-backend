package com.roundtable.roundtable.business.schedulecomment;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.domain.category.Category;
import com.roundtable.roundtable.domain.category.CategoryRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Frequency;
import com.roundtable.roundtable.domain.schedule.FrequencyType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedulecomment.Content;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleComment;
import com.roundtable.roundtable.domain.schedulecomment.ScheduleCommentRepository;
import com.roundtable.roundtable.domain.schedulecomment.dto.MemberDetailDto;
import com.roundtable.roundtable.domain.schedulecomment.dto.ScheduleCommentDetailDto;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleCommentErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ScheduleCommentServiceTest extends IntegrationTestSupport {
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
        House house = appendHouse("code");
        Member member = appendMember(house, "email");
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
        House house = appendHouse("code");
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
        House house = appendHouse("code");
        Category category = appendCategory(house);
        Member member = appendMember(house, "email");
        String contentStr = "content";

        CreateScheduleCommentDto createScheduleCommentDto = new CreateScheduleCommentDto(contentStr, member.getId(), 1L);

        //when
        assertThatThrownBy(() -> scheduleCommentService.createScheduleComment(createScheduleCommentDto))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(ScheduleErrorCode.NOT_FOUND_ID.getMessage());
    }

    @DisplayName("스케쥴 id 기반으로 댓글을 조회할 수 있다.")
    @Test
    void findByScheduleId() {
        //given
        House house = appendHouse("code");
        Member member = appendMember(house, "email");
        Category category = appendCategory(house);
        Schedule schedule = appendSchedule(category, house);
        ScheduleComment scheduleComment1 = appendScheduleComment(schedule, member, "content1");
        ScheduleComment scheduleComment2 = appendScheduleComment(schedule, member, "content2");
        ScheduleComment scheduleComment3 = appendScheduleComment(schedule, member, "content3");
        ScheduleComment scheduleComment4 = appendScheduleComment(schedule, member, "content4");

        CursorBasedRequest cursorBasedRequest = new CursorBasedRequest(0L, 4);

        AuthMember authMember = new AuthMember(member.getId(), house.getId());

        //when
        CursorBasedResponse<List<ScheduleCommentDetailDto>> scheduleCommentResponse =
                scheduleCommentService.findByScheduleId(
                        authMember, schedule.getId(), cursorBasedRequest
                );

        //then
        assertThat(scheduleCommentResponse.getLastCursorId()).isEqualTo(scheduleComment4.getId());
        assertThat(scheduleCommentResponse.getContent())
                .extracting("commentId", "content", "writer")
                .containsExactly(
                        Tuple.tuple(scheduleComment1.getId(), scheduleComment1.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName())),
                        Tuple.tuple(scheduleComment2.getId(), scheduleComment2.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName())),
                        Tuple.tuple(scheduleComment3.getId(), scheduleComment3.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName())),
                        Tuple.tuple(scheduleComment4.getId(), scheduleComment4.getContent().getContent(), new MemberDetailDto(member.getId(), member.getName()))
                );

     }

    @DisplayName("스케줄의 댓글을 읽는 member와 스케줄의 하우스가 서로 다르다면 에러를 던진다.")
    @Test
    void findByScheduleId_fail() {
        //given
        House house = appendHouse("code");
        Member member = appendMember(house, "email");
        Category category = appendCategory(house);
        Schedule schedule = appendSchedule(category, house);
        appendScheduleComment(schedule, member, "content1");
        appendScheduleComment(schedule, member, "content2");
        appendScheduleComment(schedule, member, "content3");
        appendScheduleComment(schedule, member, "content4");

        CursorBasedRequest cursorBasedRequest = new CursorBasedRequest(0L, 4);

        House house2 = appendHouse("code2");
        Member loginMember = appendMember(house2, "email2");

        AuthMember authMember = new AuthMember(loginMember.getId(), house2.getId());

        //when //then
        assertThatThrownBy(() -> scheduleCommentService.findByScheduleId(authMember, schedule.getId(), cursorBasedRequest))
                .isInstanceOf(CreateEntityException.class)
                .hasMessage(ScheduleCommentErrorCode.NOT_SAME_HOUSE.getMessage());
    }

    private Member appendMember(House house, String email) {
        Member member = Member.builder().email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private House appendHouse(String code) {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code(code).build()).build();
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

    private ScheduleComment appendScheduleComment(Schedule schedule, Member member, String content) {
        ScheduleComment scheduleComment = ScheduleComment.builder().schedule(schedule).writer(member)
                .content(Content.builder().content(content).build()).build();
        return scheduleCommentRepository.save(scheduleComment);
    }
}