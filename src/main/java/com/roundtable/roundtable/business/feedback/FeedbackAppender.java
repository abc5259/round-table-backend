package com.roundtable.roundtable.business.feedback;

import static com.roundtable.roundtable.global.exception.errorcode.FeedbackErrorCode.*;

import com.roundtable.roundtable.business.feedback.dto.CreateFeedback;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.feedback.FeedbackRepository;
import com.roundtable.roundtable.domain.feedback.FeedbackSelection;
import com.roundtable.roundtable.domain.feedback.FeedbackSelectionRepository;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedback;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedbackRepository;
import com.roundtable.roundtable.global.exception.ChoreException.NotCompletedException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.MemberException.MemberNotSameHouseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class FeedbackAppender {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackSelectionRepository feedbackSelectionRepository;
    private final PredefinedFeedbackRepository predefinedFeedbackRepository;

    public Feedback append(CreateFeedback createFeedback, Long houseId) {

        validateCompletedChore(createFeedback.chore());
        validateChoreSenderSameHouse(createFeedback.chore(), houseId);

        List<PredefinedFeedback> predefinedFeedbacks = predefinedFeedbackRepository.findByIdIn(createFeedback.predefinedFeedbackIds());
        validateAllPredefinedFeedbacksExist(createFeedback, predefinedFeedbacks);

        Feedback feedback = feedbackRepository.save(
                Feedback.create(createFeedback.emoji(), createFeedback.message(), createFeedback.chore(), createFeedback.sender())
        );
        appendFeedbackSelections(predefinedFeedbacks, feedback);

        return feedback;
    }

    private void validateCompletedChore(Chore chore) {
        if(!chore.isCompleted()) {
            throw new NotCompletedException();
        }
    }

    private void validateChoreSenderSameHouse(Chore chore, Long houseId) {
        if(!chore.isSameHouse(houseId)) {
            throw new MemberNotSameHouseException();
        }
    }

    private void validateAllPredefinedFeedbacksExist(CreateFeedback createFeedback, List<PredefinedFeedback> predefinedFeedbacks) {
        if(createFeedback.predefinedFeedbackIds().size() != predefinedFeedbacks.size()) {
            throw new NotFoundEntityException(NOT_FOUND_PREDEFINED_FEEDBACK);
        }
    }

    private void appendFeedbackSelections(List<PredefinedFeedback> predefinedFeedbacks, Feedback feedback) {
        List<FeedbackSelection> feedbackSelections = predefinedFeedbacks.stream()
                .map(predefinedFeedback -> FeedbackSelection.create(feedback, predefinedFeedback)).toList();

        feedbackSelectionRepository.saveAll(feedbackSelections);
    }
}
