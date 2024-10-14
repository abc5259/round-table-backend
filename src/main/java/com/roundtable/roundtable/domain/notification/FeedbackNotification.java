package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.sse.event.FeedbackSseEvent;
import com.roundtable.roundtable.domain.sse.event.FeedbackSseEvent.FeedbackSseData;
import com.roundtable.roundtable.domain.sse.event.SseEvent;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Values.FEEDBACK)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FeedbackNotification extends Notification {
    private Long feedbackId;
    private String scheduleName;

    @Builder
    private FeedbackNotification(Member sender,
                                Member receiver,
                                House house, Long feedbackId, String scheduleName) {
        super(sender, receiver, house);
        this.feedbackId = feedbackId;
        this.scheduleName = scheduleName;
    }

    public static FeedbackNotification create(
            Member sender,
            Member receiver,
            House house,
            Long feedbackId,
            ScheduleCompletion scheduleCompletion
    ) {
        return FeedbackNotification.builder()
                .sender(sender)
                .receiver(receiver)
                .house(house)
                .feedbackId(feedbackId)
                .scheduleName(scheduleCompletion.getScheduleName())
                .build();
    }

    public static List<FeedbackNotification> create(
            Member sender,
            List<Member> receivers,
            House house,
            Long feedbackId,
            ScheduleCompletion scheduleCompletion
    ) {
        return receivers.stream().map(receiver -> FeedbackNotification.builder()
                .sender(sender)
                .receiver(receiver)
                .house(house)
                .feedbackId(feedbackId)
                .scheduleName(scheduleCompletion.getScheduleName())
                .build()).toList();
    }

    public SseEvent toSseEvent() {
        return FeedbackSseEvent.of(getSender().getName(), feedbackId, scheduleName);
    }
}
