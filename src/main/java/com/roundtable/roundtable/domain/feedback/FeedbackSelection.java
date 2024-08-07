package com.roundtable.roundtable.domain.feedback;

import com.roundtable.roundtable.domain.common.BaseEntity;
import jakarta.persistence.Entity;
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
public class FeedbackSelection extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Feedback feedback;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private PredefinedFeedback predefinedFeedback;

    @Builder
    private FeedbackSelection(Long id, Feedback feedback, PredefinedFeedback predefinedFeedback) {
        this.id = id;
        this.feedback = feedback;
        this.predefinedFeedback = predefinedFeedback;
    }

    public static FeedbackSelection create(Feedback feedback, PredefinedFeedback predefinedFeedback) {
        return FeedbackSelection.builder()
                .feedback(feedback)
                .predefinedFeedback(predefinedFeedback)
                .build();
    }
}
