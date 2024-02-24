package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreMember;
import com.roundtable.roundtable.entity.chore.ChoreRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.ScheduleException.CreateScheduleException;
import com.roundtable.roundtable.implement.member.MemberValidator;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChoreAppender {

    private final MemberValidator memberValidator;
    private final ChoreMemberAppender choreMemberAppender;
    private final ChoreRepository choreRepository;

    public Chore appendChore(CreateChore createChore, House house) {

        checkDuplicateMember(createChore.assignedMember());
        memberValidator.validateMembersSameHouse(createChore.assignedMember(), house);

        Chore savedChore = append(createChore);

        List<ChoreMember> choreMembers = choreMemberAppender.createChoreMembers(savedChore, createChore.assignedMember());
        savedChore.addChoreMembers(choreMembers);

        return savedChore;
    }

    private static void checkDuplicateMember(List<Member> memberIds) {
        if(memberIds.size() != memberIds.stream().distinct().count()) {
            throw new CreateScheduleException("중복된 member id값이 있습니다.");
        }
    }

    private Chore append(CreateChore createChore) {
        Chore chore = Chore.create(createChore.schedule(), createChore.startDate());
        return choreRepository.save(chore);
    }
}
