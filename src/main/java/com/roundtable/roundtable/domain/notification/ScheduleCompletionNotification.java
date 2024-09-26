package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Values.SCHEDULE_COMPLETION)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ScheduleCompletionNotification extends Notification {

    private Long scheduleId;
    private String scheduleName;
    /**
     * member가 여러명일경우 ,로 구분
     */
    private String memberNames;

    @Builder
    private ScheduleCompletionNotification(Member receiver, House house, Long scheduleId, String scheduleName, String memberNames) {
        super(receiver, house);
        this.scheduleId = scheduleId;
        this.scheduleName = scheduleName;
        this.memberNames = memberNames;
    }

    public static ScheduleCompletionNotification create(Member receiver, House house, Long scheduleId, String scheduleName, List<Member> members) {
        String names = members.stream()
                .map(Member::getName) // Get name from each member
                .collect(Collectors.joining(", "));

        return ScheduleCompletionNotification.builder()
                .receiver(receiver)
                .house(house)
                .scheduleId(scheduleId)
                .scheduleName(scheduleName)
                .memberNames(names)
                .build();
    }
}
