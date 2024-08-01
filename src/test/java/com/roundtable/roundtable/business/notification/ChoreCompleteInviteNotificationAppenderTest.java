package com.roundtable.roundtable.business.notification;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.chore.ChoreMemberRepository;
import com.roundtable.roundtable.domain.chore.ChoreRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.notification.ChoreCompleteNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ChoreCompleteInviteNotificationAppenderTest extends IntegrationTestSupport {

    @Autowired
    private ChoreCompleteNotificationAppender choreCompleteNotificationAppender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ChoreRepository choreRepository;

    @Autowired
    private ChoreMemberRepository choreMemberRepository;

    @DisplayName("ChoreCompleteNotification을 추가한다.")
    @Test
    void append() {
        //given
        House house = createHouse();
        Member member1 = createMember(house, "email1");
        Member member2 = createMember(house, "email2");
        Member member3 = createMember(house, "email3");
        Member member4 = createMember(house, "email4");
        Schedule schedule = createSchedule(house);
        Chore chore = createChore(schedule);
        createChoreMember(chore, member1);
        createChoreMember(chore, member2);

        //when
        choreCompleteNotificationAppender.append(house.getId(), chore.getId(), member1.getId());

        //then
        List<ChoreCompleteNotification> notifications = notificationRepository.findAll().stream().map(n -> (ChoreCompleteNotification) n).toList();

        assertThat(notifications).hasSize(2)
                .extracting("receiver", "choreId", "choreName", "memberNames")
                .contains(
                        tuple(member3, chore.getId(), schedule.getName(), member3.getName() + "," + member4.getName()),
                        tuple(member4, chore.getId(), schedule.getName(), member3.getName() + "," + member4.getName())
                );
    }

    private House createHouse() {
        return houseRepository.save(House.builder().name("house1").inviteCode(InviteCode.builder().code("code1").build()).build());
    }

    private Member createMember(House house, String email) {
        Member member = Member.builder().name("name").email(email).house(house).password("password").build();
        return memberRepository.save(member);
    }

    private Schedule createSchedule(House house) {
        return scheduleRepository.save(Schedule.builder()
                .name("name")
                .startTime(LocalTime.of(1, 0))
                .category(Category.COOKING)
                .scheduleType(ScheduleType.ONE_TIME)
                .house(house)
                .sequence(1)
                .sequenceSize(1)
                .startDate(LocalDate.now())
                .divisionType(DivisionType.FIX)
                .build());
    }

    private Chore createChore(Schedule schedule) {
        return choreRepository.save(
                Chore.builder()
                        .isCompleted(true)
                        .startDate(LocalDate.now())
                        .schedule(schedule)
                        .completedImageUrl("imageUrl")
                        .build());
    }

    private void createChoreMember(Chore chore, Member member) {
        choreMemberRepository.save(ChoreMember.builder().chore(chore).member(member).build());
    }

}