package com.roundtable.roundtable.domain.notification;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class NotificationRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @DisplayName("Notification 수신자의 id를 이용해 Notification을 조회할 수 있다.")
    @Test
    void findTopNotificationsByReceiverId() {
        //given
        House house = createHouse();
        Member sender = createMember("email1");
        Member receiver = createMember("email2");

        for(int i=1; i<=5; i++) {
            notificationRepository.save(new Notification(sender, receiver, house));
        }

        //when
        List<Notification> notifications = notificationRepository.findTopNotificationsByReceiverId(
                receiver.getId(),
                house.getId(),
                PageRequest.of(0, 10));

        //then
        assertThat(notifications).hasSize(5)
                .extracting("sender", "receiver")
                .contains(Tuple.tuple(sender, receiver));

        for(int i=1; i<notifications.size(); i++) {
            Notification notification = notifications.get(i);
            Notification prevNotification = notifications.get(i-1);
            assertThat(prevNotification.getId()).isGreaterThan(notification.getId());
        }
    }

    @DisplayName("Notification 수신자의 id와 마지막으로 조회된 Notification id를 이용해 Notification을 조회할 수 있다.")
    @Test
    void findNextNotificationsByReceiverId() {
        //given
        House house = createHouse();
        Member sender = createMember("email1");
        Member receiver = createMember("email2");

        for(int i=1; i<=10; i++) {
            notificationRepository.save(new Notification(sender, receiver, house));
        }
        List<Notification> topNotifications = notificationRepository.findTopNotificationsByReceiverId(2L, house.getId(),
                PageRequest.of(0, 5));
        Long lastId = topNotifications.get(topNotifications.size() - 1).getId();

        //when
        List<Notification> notifications = notificationRepository.findNextNotificationsByReceiverId(
                receiver.getId(),
                house.getId(),
                lastId,
                PageRequest.of(0, 5));

        //then
        assertThat(notifications).hasSize(5)
                .extracting("sender", "receiver")
                .contains(Tuple.tuple(sender, receiver));

        for (Notification notification : notifications) {
            assertThat(notification.getId()).isLessThan(lastId);
        }

        for(int i=1; i<notifications.size(); i++) {
            Notification notification = notifications.get(i);
            Notification prevNotification = notifications.get(i-1);
            assertThat(prevNotification.getId()).isGreaterThan(notification.getId());
        }
    }

    private House createHouse() {
        House house = House.builder().name("house").inviteCode(InviteCode.of("code")).build();
        return houseRepository.save(house);
    }

    private Member createMember(String email) {
        Member member = Member.builder().email(email).password("password").build();
        return memberRepository.save(member);
    }
}