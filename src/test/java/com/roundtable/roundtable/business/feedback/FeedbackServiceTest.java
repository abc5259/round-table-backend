package com.roundtable.roundtable.business.feedback;

import static com.roundtable.roundtable.global.exception.errorcode.FeedbackErrorCode.NOT_FOUND_PREDEFINED_FEEDBACK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.feedback.dto.CreateFeedback;
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
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
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

    @BeforeEach
    public void setUp() {
        createPredefinedFeedbacks();
    }

    @DisplayName("Feedback을 추가할 수 있다.")
    @Test
    void append() {
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Schedule schedule = createSchedule(house);
        ScheduleCompletion scheduleCompletion = createScheduleCompletion(schedule);

        CreateFeedbackServiceDto createFeedbackServiceDto = new CreateFeedbackServiceDto(Emoji.FIRE, "좋아요", sender.getId(), scheduleCompletion.getId(), List.of(1, 2));

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
        assertThat(events.stream(CreateFeedbackEvent.class).count()).isEqualTo(1);

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

    public void createPredefinedFeedbacks() {
        String[] feedbackTexts = {"good", "god", "so good"};

        for (int i=0; i<3; i++) {

            predefinedFeedbackRepository.save(PredefinedFeedback.builder().id(i+1).feedbackText(feedbackTexts[i]).build());
        }
    }
}