package com.roundtable.roundtable.business.feedback;

import com.roundtable.roundtable.business.chore.ChoreReader;
import com.roundtable.roundtable.business.feedback.dto.CreateFeedbackServiceDto;
import com.roundtable.roundtable.business.feedback.event.CreateFeedbackEvent;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.schedule.ScheduleReader;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.FeedbackException;
import com.roundtable.roundtable.global.exception.errorcode.FeedbackErrorCode;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackAppender feedbackAppender;

    private final MemberReader memberReader;

    private final ScheduleReader scheduleReader;

    private final ScheduleCompletionRepository scheduleCompletionRepository;

    private final ScheduleCompletionMemberRepository scheduleCompletionMemberRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long createFeedback(CreateFeedbackServiceDto createFeedbackServiceDto, Long houseId) {
        Member sender = memberReader.findById(createFeedbackServiceDto.senderId());
        ScheduleCompletion scheduleCompletion = scheduleCompletionRepository.findById(createFeedbackServiceDto.scheduleCompletionId())
                .orElseThrow(() -> new FeedbackException(FeedbackErrorCode.NOT_COMPLETION_SCHEDULE));

        Feedback feedback = feedbackAppender.append(createFeedbackServiceDto.toCreateFeedback(sender, scheduleCompletion));

        applicationEventPublisher.publishEvent(
                new CreateFeedbackEvent(
                        feedback.getId(),
                        createFeedbackServiceDto.senderId(),
                        createFeedbackServiceDto.scheduleCompletionId(),
                        houseId
                )
        );

        return feedback.getId();
    }
}
