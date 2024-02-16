package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.implement.member.MemberException.MemberNotFoundException;
import com.roundtable.roundtable.implement.member.MemberException.MemberNotSameHouseException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberReader {
    private final MemberValidator memberValidator;
    private final MemberRepository memberRepository;

    public boolean isExistEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public void checkDuplicateEmail(String email) {
        if(isExistEmail(email)) {
            throw new MemberException.EmailDuplicatedException(email);
        }
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<Member> findAllById(List<Long> membersId) {
        List<Member> findMembers = memberRepository.findAllById(membersId);

        if(findMembers.size() != membersId.size()) {
            throw new MemberNotFoundException();
        }

        return findMembers;
    }
}
