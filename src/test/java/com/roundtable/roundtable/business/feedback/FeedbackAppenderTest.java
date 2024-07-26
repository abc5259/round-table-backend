package com.roundtable.roundtable.business.feedback;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
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
        House house = createHouse();
        Member member1 = createMember("email1", house);
        Member member2 = createMember("email2", house);
        Schedule schedule = createSchedule(house);
        Chore chore = createChore(schedule, true);

        CreateFeedback createFeedback = new CreateFeedback(Emoji.FIRE, "좋아요", member1, member2, chore, List.of(1, 2));

        //when
        Feedback result = feedbackAppender.append(createFeedback);

        //then
        List<Feedback> feedbacks = feedbackRepository.findAll();
        List<FeedbackSelection> feedbackSelections = feedbackSelectionRepository.findAll();

        assertThat(feedbacks).hasSize(1);
        assertThat(feedbacks.get(0))
                .extracting("emoji", "message", "sender", "receiver", "chore")
                .contains(
                        Emoji.FIRE,
                        "좋아요",
                        member1,
                        member2,
                        chore
                );
        assertThat(feedbackSelections).hasSize(2)
                .extracting("feedback")
                .contains(result, result);
    }

    public House createHouse() {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code("code").build()).build();
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