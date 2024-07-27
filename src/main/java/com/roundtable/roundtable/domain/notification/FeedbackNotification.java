package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
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
    private String choreName;

    @Builder
    private FeedbackNotification(Member sender,
                                Member receiver,
                                House house, Long feedbackId, String choreName) {
        super(sender, receiver, house);
        this.feedbackId = feedbackId;
        this.choreName = choreName;
    }

    public static FeedbackNotification create(
            Member sender,
            Member receiver,
            House house,
            Long feedbackId,
            String choreName
    ) {
        return FeedbackNotification.builder()
                .sender(sender)
                .receiver(receiver)
                .house(house)
                .feedbackId(feedbackId)
                .choreName(choreName)
                .build();
    }
}
