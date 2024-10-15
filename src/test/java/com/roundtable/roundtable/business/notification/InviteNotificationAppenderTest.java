package com.roundtable.roundtable.business.notification;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.HouseErrorCode;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class InviteNotificationAppenderTest extends IntegrationTestSupport {

    @Autowired
    private InviteNotificationAppender inviteNotificationAppender;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("초대 알림을 만들 수 있다.")
    @Test
    void appendInviteNotification() {
        //given
        House house = appendHouse();
        Member member1 = appendMember(house, "member1@domian.com");
        Member member2 = appendMember(null, "member2@domian.com");
        Member member3 = appendMember(null, "member3@domian.com");
        Member member4 = appendMember(house, "member4@domian.com");

        CreateInviteNotification createInviteNotification = new CreateInviteNotification(
                member1.getId(),
                house.getId(),
                List.of(member2.getEmail(), member3.getEmail(), member4.getEmail())
        );

        //when
        inviteNotificationAppender.append(createInviteNotification);

        //then
        List<Notification> notifications = notificationRepository.findAll();

        List<Member> expectedReceivers = List.of(member2, member3);
        assertThat(notifications).hasSize(expectedReceivers.size());
        notifications.forEach(notification -> assertThat(expectedReceivers.contains(notification.getReceiver())).isTrue());
    }

    @DisplayName("하우스 초대 알림을 만들때 하우스가 존재하지 않는다면 에러를 던진다.")
    @Test
    void appendInviteNotification_no_house() {
        //given
        Member member1 = appendMember(null, "member1@domian.com");
        Member member2 = appendMember(null, "member2@domian.com");
        Member member3 = appendMember(null, "member3@domian.com");
        Member member4 = appendMember(null, "member4@domian.com");

        CreateInviteNotification createInviteNotification = new CreateInviteNotification(
                member1.getId(),
                1L,
                List.of(member2.getEmail(), member3.getEmail(), member4.getEmail())
        );

        //when //then
        assertThatThrownBy(() -> inviteNotificationAppender.append(createInviteNotification))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(HouseErrorCode.NOT_FOUND.getMessage());
    }

    @DisplayName("하우스 초대 알림을 만들때 sender가 존재하지 않다면 에러를 던진다.")
    @Test
    void test() {
        //given
        House house = appendHouse();
        Member member1 = appendMember(house, "member1@domian.com");
        Member member2 = appendMember(null, "member2@domian.com");
        Member member3 = appendMember(null, "member3@domian.com");
        Member member4 = appendMember(null, "member4@domian.com");

        CreateInviteNotification createInviteNotification = new CreateInviteNotification(
                member1.getId() - 1,
                house.getId(),
                List.of(member2.getEmail(), member3.getEmail(), member4.getEmail())
        );

        //when //then
        assertThatThrownBy(() -> inviteNotificationAppender.append(createInviteNotification))
                .isInstanceOf(NotFoundEntityException.class)
                .hasMessage(MemberErrorCode.NOT_FOUND.getMessage());
    }

    private Member appendMember(House house, String email) {
        Member member = Member.builder().name("name").email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private House appendHouse() {
        House house = House.builder().name("house").inviteCode(InviteCode.builder().code("code").build()).build();
        return houseRepository.save(house);
    }
}