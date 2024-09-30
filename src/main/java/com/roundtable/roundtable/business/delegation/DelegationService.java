package com.roundtable.roundtable.business.delegation;

import com.roundtable.roundtable.business.delegation.dto.CreateDelegationDto;
import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationRepository;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleDay;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.global.exception.DelegationException;
import com.roundtable.roundtable.global.exception.errorcode.DelegationErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DelegationService {

    private final ScheduleCompletionRepository scheduleCompletionRepository;
    private final ScheduleDayRepository scheduleDayRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final DelegationRepository delegationRepository;

    public Long createDelegation(CreateDelegationDto createDelegationDto) {
        /**
         * [x] 이미 부탁한건 아닌지
         * [x] 오늘 수행하는 스케줄인지
         * [x] 완료된 스케줄이 아닌지
         */
        validateCreateDelegation(createDelegationDto);

        List<ScheduleMember> scheduleManagers = scheduleMemberRepository.findByScheduleManagers(createDelegationDto.scheduleId());
        Delegation delegation = Delegation.create(
                scheduleManagers,
                createDelegationDto.message(),
                Schedule.Id(createDelegationDto.scheduleId()),
                Member.Id(createDelegationDto.senderId()),
                Member.Id(createDelegationDto.receiverId()),
                createDelegationDto.delegationDate()
        );
        delegationRepository.save(delegation);

        return delegation.getId();
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
