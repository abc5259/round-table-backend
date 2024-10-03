package com.roundtable.roundtable.business.feedback;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.feedback.dto.CreateFeedbackServiceDto;
import com.roundtable.roundtable.business.feedback.event.CreateFeedbackEvent;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.feedback.FeedbackRepository;
import com.roundtable.roundtable.domain.feedback.FeedbackSelection;
import com.roundtable.roundtable.domain.feedback.FeedbackSelectionRepository;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedback;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedbackRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMember;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.global.exception.FeedbackException;
import com.roundtable.roundtable.global.exception.errorcode.FeedbackErrorCode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RecordApplicationEvents
class FeedbackServiceTest extends IntegrationTestSupport {

    @Autowired
    private FeedbackService sut;

    @Autowired
    private ApplicationEvents events;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackSelectionRepository feedbackSelectionRepository;

    @Autowired
    private PredefinedFeedbackRepository predefinedFeedbackRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ScheduleCompletionRepository scheduleCompletionRepository;

    @Autowired
    private ScheduleCompletionMemberRepository scheduleCompletionMemberRepository;

    @BeforeEach
    public void setUp() {
        createPredefinedFeedbacks();
    }

    @DisplayName("Feedback을 추가할 수 있다.")
    @Test
    void append() {
        /**
         * 피드백은 어디에 줄 수 있을까?
         * 일단 완료된 스케줄에 준다.
         * 완료된 스케줄에서 자기가 완료한 스케줄에는 줄 수 없다.
         * 다른 하우스의 스케줄에도 피드백을 주면 안됨
         */
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Member scheduleCompletionMember = createMember("email2", house);
        Schedule schedule = createSchedule(house);
        ScheduleCompletion scheduleCompletion = createScheduleCompletion(schedule);
        createScheduleCompletionMember(scheduleCompletion, scheduleCompletionMember);

        CreateFeedbackServiceDto createFeedbackServiceDto = new CreateFeedbackServiceDto(Emoji.FIRE, "좋아요", sender.getId(), schedule.getId(), scheduleCompletion.getId(), List.of(1, 2));

        //when
        Long feedbackId = sut.createFeedback(createFeedbackServiceDto, house.getId());

        //then
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow();
        List<FeedbackSelection> feedbackSelections = feedbackSelectionRepository.findAll();

        assertThat(feedback)
                .extracting("emoji", "message", "sender", "scheduleCompletion")
                .contains(
                        Emoji.FIRE,
                        "좋아요",
                        sender,
                        scheduleCompletion
                );
        assertThat(feedbackSelections).hasSize(2)
                .extracting("feedback")
                .contains(feedback, feedback);
        assertThat(events.stream(CreateFeedbackEvent.class)).hasSize(1)
                .anySatisfy(event -> {
                    assertAll(
                            () -> assertThat(event.houseId()).isEqualTo(house.getId()),
                            () -> assertThat(event.feedbackId()).isEqualTo(feedback.getId()),
                            () -> assertThat(event.senderId()).isEqualTo(sender.getId()),
                            () -> assertThat(event.scheduleCompletionId()).isEqualTo(scheduleCompletion.getId())
                    );
                });
    }

    @DisplayName("완료된 스케줄이 아니라면 예외가 발생한다.")
    @Test
    void append_feedback_when_not_completion_schedule_throw_exception() {
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Schedule schedule = createSchedule(house);

        CreateFeedbackServiceDto createFeedbackServiceDto = new CreateFeedbackServiceDto(Emoji.FIRE, "좋아요", sender.getId(), schedule.getId(), 1L, List.of(1, 2));

        //when //then
        assertThatThrownBy(() -> sut.createFeedback(createFeedbackServiceDto, house.getId()))
                .isInstanceOf(FeedbackException.class)
                .hasMessageContaining(FeedbackErrorCode.NOT_COMPLETION_SCHEDULE.getMessage());
    }

    public House createHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    public Member createMember(String email, House house) {
        Member member = Member.builder().email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Schedule createSchedule(House house) {
        Schedule schedule = Schedule.builder()
                .name("name")
                .startTime(LocalTime.of(1, 0))
                .category(Category.COOKING)
                .house(house)
                .sequence(1)
                .sequenceSize(1)
                .startDate(LocalDate.now())
                .divisionType(DivisionType.FIX)
                .scheduleType(ScheduleType.REPEAT)
                .build();
        return scheduleRepository.save(schedule);
    }

    private ScheduleCompletion createScheduleCompletion(Schedule schedule) {
        ScheduleCompletion scheduleCompletion = ScheduleCompletion.builder().schedule(schedule)
                .completionDate(LocalDate.now()).sequence(1).build();
        return scheduleCompletionRepository.save(scheduleCompletion);
    }

    private ScheduleCompletionMember createScheduleCompletionMember(ScheduleCompletion scheduleCompletion, Member member) {
        ScheduleCompletionMember scheduleCompletionMember = ScheduleCompletionMember.builder()
                .scheduleCompletion(scheduleCompletion)
                .member(member)
                .build();
        return scheduleCompletionMemberRepository.save(scheduleCompletionMember);
    }

    public void createPredefinedFeedbacks() {
        String[] feedbackTexts = {"good", "god", "so good"};

        for (int i=0; i<3; i++) {

            predefinedFeedbackRepository.save(PredefinedFeedback.builder().id(i+1).feedbackText(feedbackTexts[i]).build());
        }
    }
}