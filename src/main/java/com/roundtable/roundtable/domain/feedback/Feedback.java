package com.roundtable.roundtable.domain.feedback;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Emoji emoji;

    @NotNull
    @Column
    private String message;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Chore chore;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @Builder
    private Feedback(Long id, Emoji emoji, String message, Chore chore, Member sender, Member receiver) {
        this.id = id;
        this.emoji = emoji;
        this.message = message;
        this.chore = chore;
        this.sender = sender;
        this.receiver = receiver;
    }

    public static Feedback create(Emoji emoji, String message, Chore chore, Member sender, Member receiver) {
        return Feedback.builder()
                .emoji(emoji)
                .message(message)
                .chore(chore)
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
