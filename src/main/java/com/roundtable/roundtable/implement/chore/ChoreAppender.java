package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreRepository;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.ScheduleException.CreateScheduleException;
import com.roundtable.roundtable.implement.member.MemberReader;
import com.roundtable.roundtable.implement.member.MemberValidator;
import com.roundtable.roundtable.implement.schedule.CreateSchedule;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChoreAppender {

    private final MemberReader memberReader;
    private final MemberValidator memberValidator;
    private final ChoreMemberAppender choreMemberAppender;
    private final ChoreRepository choreRepository;

    public Chore appendChore(CreateChore createChore, House house) {

        checkDuplicateMemberId(createChore.assignedMemberIds());
        List<Member> assignedMembers = memberReader.findAllById(createChore.assignedMemberIds());
        memberValidator.validateMembersSameHouse(assignedMembers, house);

        Chore savedChore = append(createChore);

        choreMemberAppender.createChoreMembers(savedChore,assignedMembers);

        return savedChore;
    }

    private static void checkDuplicateMemberId(List<Long> memberIds) {
        if(memberIds.size() != memberIds.stream().distinct().count()) {
            throw new CreateScheduleException("중복된 member id값이 있습니다.");
        }
    }

    private Chore append(CreateChore createChore) {
        Chore chore = Chore.create(createChore.schedule());
        return choreRepository.save(chore);
    }
}
