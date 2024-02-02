package com.roundtable.roundtable.implement.member;

import com.roundtable.roundtable.entity.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberReader {
    private final MemberRepository memberRepository;

    public boolean isExistEmail(String email) {
        return memberRepository.existsByEmail(email);
    }
}
