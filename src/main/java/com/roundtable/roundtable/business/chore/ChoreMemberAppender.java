package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.chore.ChoreMemberRepository;
import com.roundtable.roundtable.domain.member.Member;
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

        return choreMemberRepository.saveAllAndFlush(choreMembers);
    }
}
