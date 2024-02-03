package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.member.MemberRepository;
import com.roundtable.roundtable.implement.member.MemberException.MemberNotFoundException;
import com.roundtable.roundtable.implement.member.MemberException.MemberUnAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReader {
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

}
