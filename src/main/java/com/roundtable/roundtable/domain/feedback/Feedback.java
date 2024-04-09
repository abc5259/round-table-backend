package com.roundtable.roundtable.domain.feedback;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String emojiUrl;

    @NotNull
    private String message;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Chore chore;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member feedbackBy;
}
