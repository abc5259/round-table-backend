package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Values.CHORE_COMPLETE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChoreCompleteNotification extends Notification {

    private Long choreId;
    private String choreName;
    /**
     * member가 여러명일경우 ,로 구분
     */
    private String memberNames;

    @Builder
    private ChoreCompleteNotification(Member sender, Member receiver, Long choreId, String choreName, String memberNames) {
        super(sender, receiver);
        this.choreId = choreId;
        this.choreName = choreName;
        this.memberNames = memberNames;
    }

    public static ChoreCompleteNotification create(Member sender, Member receiver, Long choreId, String choreName, List<String> memberNames) {
        return ChoreCompleteNotification.builder()
                .sender(sender)
                .receiver(receiver)
                .choreId(choreId)
                .choreName(choreName)
                .memberNames(String.join(",", memberNames))
                .build();
    }
}
