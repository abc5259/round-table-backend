package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.schedule.dto.CreateScheduleDto;
import com.roundtable.roundtable.business.schedule.dto.ScheduleOfMemberResponse;
import com.roundtable.roundtable.business.schedule.dto.ScheduleResponse;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleDay;
import com.roundtable.roundtable.domain.schedule.ScheduleDayRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.ScheduleMemberRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleReader scheduleReader;
    private final MemberReader memberReader;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ScheduleDayRepository scheduleDayRepository;

    public Long create(CreateScheduleDto createScheduleDto, AuthMember authMember) {
        House house = House.Id(authMember.houseId());
        List<Member> members = memberReader.findAllByIdOrThrow(createScheduleDto.memberIds());

        Schedule schedule = Schedule.create(
                createScheduleDto.name(),
                createScheduleDto.startDate(),
                createScheduleDto.startTime(),
                createScheduleDto.divisionType(),
                createScheduleDto.scheduleType(),
                house,
                members.size(),
                createScheduleDto.category()
        );
        List<ScheduleMember> scheduleMembers = ScheduleMember.createScheduleMembers(createScheduleDto.divisionType(), members, schedule);
        List<ScheduleDay> scheduleDays = ScheduleDay.createScheduleDays(schedule, createScheduleDto.days());

        scheduleRepository.save(schedule);
        scheduleMemberRepository.saveAll(scheduleMembers);
        scheduleDayRepository.saveAll(scheduleDays);

        return schedule.getId();
    }

    public CursorBasedResponse<List<ScheduleResponse>> findSchedulesByDate(AuthMember authMember, LocalDate date, CursorBasedRequest cursorBasedRequest) {
        return scheduleReader.findHomeSchedulesByDate(authMember.houseId(), date, cursorBasedRequest);
    }

    public CursorBasedResponse<List<ScheduleOfMemberResponse>> findMemberSchedulesByDate(AuthMember authMember, LocalDate date, CursorBasedRequest cursorBasedRequest) {
        return scheduleReader.findMemberSchedulesByDate(authMember.houseId(), date, authMember.memberId(), cursorBasedRequest);
    }
}
