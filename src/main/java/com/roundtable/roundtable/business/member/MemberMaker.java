package com.roundtable.roundtable.business.member;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberMaker {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long register(RegisterMember registerMember) {
        Member member = Member.of(registerMember.email(), registerMember.password(), passwordEncoder);
        Member newMember = memberRepository.save(member);

        return newMember.getId();
    }
}
