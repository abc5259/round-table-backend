package com.roundtable.roundtable.business.notification;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.feedback.FeedbackRepository;
import com.roundtable.roundtable.domain.feedback.FeedbackSelectionRepository;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedback;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedbackRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMember;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class FeedbackNotificationAppenderTest extends IntegrationTestSupport {

    @Autowired
    private FeedbackRepository feedbackRepository;

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

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private FeedbackNotificationAppender sut;

    @DisplayName("피드백 알림을 생성한다.")
    @Test
    void append() {
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Member scheduleCompletionMember1 = createMember("email2", house);
        Member scheduleCompletionMember2 = createMember("email3", house);
        Schedule schedule = createSchedule(house);
        ScheduleCompletion scheduleCompletion = createScheduleCompletion(schedule);
        createScheduleCompletionMember(scheduleCompletion, scheduleCompletionMember1);
        createScheduleCompletionMember(scheduleCompletion, scheduleCompletionMember2);
        Feedback feedback = createFeedback(scheduleCompletion, sender);

        //when
        sut.append(feedback.getId(), house.getId(), scheduleCompletion.getId(), sender.getId());

        //then
        List<FeedbackNotification> notifications = notificationRepository.findAll().stream().map(notification -> (FeedbackNotification)notification).toList();
        assertThat(notifications).hasSize(2)
                .extracting("sender", "receiver", "feedbackId", "scheduleName")
                .containsExactly(
                        Tuple.tuple(sender, scheduleCompletionMember1, feedback.getId(), schedule.getName()),
                        Tuple.tuple(sender, scheduleCompletionMember2, feedback.getId(), schedule.getName())
                );
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

    private Feedback createFeedback(ScheduleCompletion scheduleCompletion, Member sender) {
        Feedback feedback = Feedback.builder()
                .emoji(Emoji.FIRE)
                .message("msg")
                .scheduleCompletion(scheduleCompletion)
                .sender(sender)
                .build();
        return feedbackRepository.save(feedback);
    }

    public void createPredefinedFeedbacks() {
        String[] feedbackTexts = {"good", "god", "so good"};

        for (int i=0; i<3; i++) {

            predefinedFeedbackRepository.save(PredefinedFeedback.builder().id(i+1).feedbackText(feedbackTexts[i]).build());
        }
    }


}