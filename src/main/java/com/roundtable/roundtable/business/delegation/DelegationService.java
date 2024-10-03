package com.roundtable.roundtable.business.delegation;

import com.roundtable.roundtable.business.delegation.dto.CreateDelegationDto;
import com.roundtable.roundtable.business.delegation.event.CreateDelegationEvent;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationRepository;
import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.DelegationException;
import com.roundtable.roundtable.global.exception.errorcode.DelegationErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DelegationService {

    private final ScheduleCompletionRepository scheduleCompletionRepository;
    private final ScheduleDayRepository scheduleDayRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final MemberReader memberReader;
    private final DelegationRepository delegationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long createDelegation(Long houseId, CreateDelegationDto createDelegationDto) {
        validateCreateDelegation(createDelegationDto);

        Member sender = memberReader.findById(createDelegationDto.senderId());
        Member receiver = memberReader.findById(createDelegationDto.receiverId());
        List<ScheduleMember> scheduleManagers = scheduleMemberRepository.findByScheduleManagers(createDelegationDto.scheduleId());
        Delegation delegation = Delegation.create(
                scheduleManagers,
                createDelegationDto.message(),
                Schedule.Id(createDelegationDto.scheduleId()),
                sender,
                receiver,
                createDelegationDto.delegationDate()
        );
        delegationRepository.save(delegation);
        applicationEventPublisher.publishEvent(new CreateDelegationEvent(houseId, sender, receiver, createDelegationDto.scheduleId(), delegation));
        return delegation.getId();
    }

    @Transactional
    public void updateDelegationStatus(Long memberId, Long delegationId, DelegationStatus delegationStatus) {
        Delegation delegation = delegationRepository.findById(delegationId).orElseThrow(NotFoundEntityException::new);
        delegation.updateStatus(memberId, delegationStatus);
    }

    private void validateCreateDelegation(CreateDelegationDto createDelegationDto) {
        validateAlreadyExistDelegation(createDelegationDto);
        validateTodaySchedule(createDelegationDto);
        validateAlreadyCompletionSchedule(createDelegationDto);
    }

    private void validateAlreadyExistDelegation(CreateDelegationDto createDelegationDto) {
        if(delegationRepository.existsByScheduleIdAndSenderIdAndDelegationDate(
                createDelegationDto.scheduleId(),
                createDelegationDto.senderId(),
                createDelegationDto.delegationDate())
        ) {
            throw new DelegationException(DelegationErrorCode.ALREADY_EXIST_DELEGATION);
        }
    }

    private void validateTodaySchedule(CreateDelegationDto createDelegationDto) {
        Day day = Day.forDayOfWeek(createDelegationDto.delegationDate().getDayOfWeek());
        if(!scheduleDayRepository.existsByScheduleIdAndDayOfWeek(createDelegationDto.scheduleId(), day)) {
            throw new DelegationException(DelegationErrorCode.DELEGATION_FORBIDDEN_NOT_TODAY_SCHEDULE);
        }
    }

    private void validateAlreadyCompletionSchedule(CreateDelegationDto createDelegationDto) {
        if(scheduleCompletionRepository.existsByScheduleIdAndCompletionDate(createDelegationDto.scheduleId(), createDelegationDto.delegationDate())) {
            throw new DelegationException(DelegationErrorCode.DELEGATION_FORBIDDEN_ALREADY_COMPLETION_SCHEDULE);
        }
    }

}
