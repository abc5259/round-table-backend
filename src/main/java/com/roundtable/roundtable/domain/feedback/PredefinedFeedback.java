package com.roundtable.roundtable.domain.feedback;

import com.roundtable.roundtable.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PredefinedFeedback extends BaseEntity {
    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String feedbackText;

    @Builder
    private PredefinedFeedback(Integer id, String feedbackText) {
        this.id = id;
        this.feedbackText = feedbackText;
    }
}
