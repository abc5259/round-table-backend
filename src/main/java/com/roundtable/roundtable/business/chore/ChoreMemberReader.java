package com.roundtable.roundtable.business.chore;


import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreMember;
import com.roundtable.roundtable.domain.chore.ChoreMemberRepository;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoreMemberReader {

    private final ChoreMemberRepository choreMemberRepository;

    public boolean existByMemberIdAndChoreId(Long memberId, Long choreId) {
        return choreMemberRepository
                .existsChoreMemberByMemberAndChore(Member.Id(memberId), Chore.Id(choreId));
    }

}
