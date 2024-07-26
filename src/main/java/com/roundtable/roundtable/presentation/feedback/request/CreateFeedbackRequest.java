package com.roundtable.roundtable.presentation.feedback.request;

import com.roundtable.roundtable.business.feedback.dto.CreateFeedbackServiceDto;
import com.roundtable.roundtable.domain.feedback.Emoji;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateFeedbackRequest(
        @NotNull
        Emoji emoji,
        @NotBlank
        String message,
        @NotNull
        Long senderId,
        @NotNull
        Long choreId,
        @Min(0)
        List<Integer> predefinedFeedbackIds
) {
    public CreateFeedbackServiceDto toCreateFeedbackServiceDto() {
        return new CreateFeedbackServiceDto(
                emoji,
                message,
                senderId,
                choreId,
                predefinedFeedbackIds
        );
    }
}
