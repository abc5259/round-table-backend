package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreMember;
import com.roundtable.roundtable.entity.chore.ChoreMemberRepository;
import com.roundtable.roundtable.entity.member.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChoreMemberAppender {
    private final ChoreMemberRepository choreMemberRepository;

    public List<ChoreMember> createChoreMembers(Chore chore, List<Member> members) {
        List<ChoreMember> choreMembers = members.stream()
                .map(member -> ChoreMember.create(chore, member))
                .toList();

        return choreMemberRepository.saveAll(choreMembers);
    }
}
