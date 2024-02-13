package com.roundtable.roundtable.entity.chore;

import com.roundtable.roundtable.entity.member.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
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
