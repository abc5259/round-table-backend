package com.roundtable.roundtable.business.feedback;

import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.feedback.dto.CreateFeedbackServiceDto;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackAppender feedbackAppender;
    private final MemberReader memberReader;
    private final ChoreReader choreReader;

    public Long createFeedback(CreateFeedbackServiceDto createFeedbackServiceDto, Long houseId) {
        Member sender = memberReader.findById(createFeedbackServiceDto.senderId());
        Chore chore = choreReader.readById(createFeedbackServiceDto.choreId());
        Feedback feedback = feedbackAppender.append(createFeedbackServiceDto.toCreateFeedback(sender, chore), houseId);
        return feedback.getId();
    }
}
