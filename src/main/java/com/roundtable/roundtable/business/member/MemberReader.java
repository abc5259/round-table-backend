package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.global.exception.CoreException.DuplicatedException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.errorcode.MemberErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberReader {

    private final MemberRepository memberRepository;

    public boolean existEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean nonExistMemberId(Long memberId) {
        return !memberRepository.existsById(memberId);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException(MemberErrorCode.NOT_FOUND));
    }

    public List<Member> findByEmail(List<String> emails) {
        return memberRepository.findByEmailIn(emails);
    }

    public List<Member> findAllById(List<Long> membersId) {
        List<Member> findMembers = memberRepository.findAllById(membersId);

        if(findMembers.size() != membersId.size()) {
            throw new NotFoundEntityException(MemberErrorCode.NOT_FOUND);
        }

        return findMembers;
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundEntityException(MemberErrorCode.NOT_FOUND));
    }
}
