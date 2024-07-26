package com.roundtable.roundtable.business.feedback;

import static com.roundtable.roundtable.global.exception.errorcode.FeedbackErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.feedback.dto.CreateFeedback;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreRepository;
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
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.global.exception.ChoreException.NotCompletedException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class FeedbackAppenderTest extends IntegrationTestSupport {

    @Autowired
    private FeedbackAppender feedbackAppender;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackSelectionRepository feedbackSelectionRepository;

    @Autowired
    private PredefinedFeedbackRepository predefinedFeedbackRepository;

    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

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
        Chore chore = createChore(schedule, true);

        CreateFeedback createFeedback = new CreateFeedback(Emoji.FIRE, "좋아요", sender, chore, List.of(1, 2));

        //when
        Feedback result = feedbackAppender.append(createFeedback, house.getId());

        //then
        List<Feedback> feedbacks = feedbackRepository.findAll();
        List<FeedbackSelection> feedbackSelections = feedbackSelectionRepository.findAll();

        assertThat(feedbacks).hasSize(1);
        assertThat(feedbacks.get(0))
                .extracting("emoji", "message", "sender", "chore")
                .contains(
                        Emoji.FIRE,
                        "좋아요",
                        sender,
                        chore
                );
        assertThat(feedbackSelections).hasSize(2)
                .extracting("feedback")
                .contains(result, result);
    }

    @DisplayName("완료되지 않은 집안일이라면 피드백을 생성할 수 없다.")
    @Test
    void append_with_not_completed_feedback() {
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Schedule schedule = createSchedule(house);
        Chore chore = createChore(schedule, false);

        CreateFeedback createFeedback = new CreateFeedback(Emoji.FIRE, "좋아요", sender, chore, List.of(1, 2));

        //when //then
        assertThatThrownBy(() -> feedbackAppender.append(createFeedback, house.getId()))
                .isInstanceOf(NotCompletedException.class);
    }

    @DisplayName("존재하지 않은 기본 탬플릿 피드백을 준다면 예외를 던진다.")
    @Test
    void append_with_not_exist_predefined_feedback() {
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Schedule schedule = createSchedule(house);
        Chore chore = createChore(schedule, true);

        CreateFeedback createFeedback = new CreateFeedback(Emoji.FIRE, "좋아요", sender, chore, List.of(4));

        //when //then
        assertThatThrownBy(() -> feedbackAppender.append(createFeedback, house.getId()))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(NOT_FOUND_PREDEFINED_FEEDBACK.getMessage());
    }

    @DisplayName("다른 하우스의 집안일에 대해 피드백을 줄 수 없다.")
    @Test
    void append_with_not_same_member_chore_house() {
        //given
        House house = createHouse("code");
        House house2 = createHouse("code2");
        Member sender = createMember("email1", house);
        Schedule schedule = createSchedule(house2);
        Chore chore = createChore(schedule, true);

        CreateFeedback createFeedback = new CreateFeedback(Emoji.FIRE, "좋아요", sender, chore, List.of(1,2));

        //when //then
        assertThatThrownBy(() -> feedbackAppender.append(createFeedback, house.getId()))
                .isInstanceOf(MemberNotSameHouseException.class);
    }

    public House createHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    public Member createMember(String email, House house) {
        Member member = Member.builder().email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Chore createChore(Schedule schedule, boolean isCompleted) {
        Chore chore = Chore.builder()
                .startDate(LocalDate.now())
                .schedule(schedule)
                .isCompleted(isCompleted)
                .build();
        return choreRepository.save(chore);
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

    public void createPredefinedFeedbacks() {
        String[] feedbackTexts = {"good", "god", "so good"};

        for (int i=0; i<3; i++) {

            predefinedFeedbackRepository.save(PredefinedFeedback.builder().id(i+1).feedbackText(feedbackTexts[i]).build());
        }
    }
}