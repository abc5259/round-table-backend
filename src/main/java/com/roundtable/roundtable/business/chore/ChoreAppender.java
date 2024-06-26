package com.roundtable.roundtable.business.chore;

import static com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode.DUPLICATED_MEMBER_ID;

import com.roundtable.roundtable.business.chore.dto.CreateChore;
import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreBulkRepository;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.chore.ChoreRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import com.roundtable.roundtable.global.exception.CoreException.CreateEntityException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChoreAppender {

    private final MemberValidator memberValidator;

    private final ChoreMemberAppender choreMemberAppender;

    private final ChoreMapper choreMapper;

    private final ChoreRepository choreRepository;

    private final ChoreBulkRepository choreBulkRepository;

    public Chore appendChore(CreateChore createChore, House house) {

        checkDuplicateMember(createChore.assignedMember());
        memberValidator.validateMembersSameHouse(createChore.assignedMember(), house);

        Chore savedChore = append(createChore);

        List<ChoreMember> choreMembers = choreMemberAppender.createChoreMembers(savedChore, createChore.assignedMember());
        savedChore.addChoreMembers(choreMembers);

        return savedChore;
    }

    private void checkDuplicateMember(List<Member> memberIds) {
        if(memberIds.size() != memberIds.stream().distinct().count()) {
            throw new CreateEntityException(DUPLICATED_MEMBER_ID);
        }
    }

    private Chore append(CreateChore createChore) {
        Chore chore = Chore.create(createChore.schedule(), createChore.startDate());
        return choreRepository.save(chore);
    }

    public void appendChores(Map<ScheduleIdDto, List<Member>> scheduleAllocatorsMap, LocalDate startDate) {
        appendChores(choreMapper.toEntities(scheduleAllocatorsMap, startDate));
    }

    private void appendChores(List<Chore> chores) {
        choreBulkRepository.saveAll(chores);
    }

}
