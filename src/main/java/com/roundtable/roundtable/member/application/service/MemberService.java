package com.roundtable.roundtable.member.application.service;

import com.roundtable.roundtable.member.application.authcode.AuthCode;
import com.roundtable.roundtable.member.application.authcode.AuthCodeStoreStrategy;
import com.roundtable.roundtable.member.application.dto.EmailRequest;
import com.roundtable.roundtable.member.application.dto.MemberLoginRequest;
import com.roundtable.roundtable.member.application.dto.MemberRegisterRequest;
import com.roundtable.roundtable.member.domain.Member;
import com.roundtable.roundtable.member.exception.MemberException;
import com.roundtable.roundtable.member.exception.MemberException.EmailDuplicatedException;
import com.roundtable.roundtable.member.domain.MemberRepository;
import com.roundtable.roundtable.member.exception.MemberException.MemberNotFoundException;
import com.roundtable.roundtable.member.exception.MemberException.MemberUnAuthorizationException;
import com.roundtable.roundtable.member.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final AuthCodeStoreStrategy authCodeStoreStrategy;

    @Transactional(readOnly = true)
    public void sendCodeToEmail(final EmailRequest emailRequest) {
        this.checkDuplicatedEmail(emailRequest.email());
        String title = "Round Table 이메일 인증 번호";
        AuthCode authCode = new AuthCode();

        mailService.sendEmail(emailRequest.email(), title, authCode.getCode());
        authCodeStoreStrategy.saveAuthCode(authCode);
    }

    private void checkDuplicatedEmail(String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new EmailDuplicatedException(email);
        }
    }

    public boolean isCorrectAuthCode(AuthCode authCode) {
        return authCodeStoreStrategy.isCorrectAuthCode(authCode);
    }

    public void register(final MemberRegisterRequest memberRegisterRequest) {
        checkDuplicatedEmail(memberRegisterRequest.email());
        Member member = Member.of(memberRegisterRequest.email(), memberRegisterRequest.password());
        memberRepository.save(member);
        log.info(String.format("%s 이메일을 가진 User 생성", memberRegisterRequest.email()));
    }

    @Transactional(readOnly = true)
    public void login(final MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmail(memberLoginRequest.email())
                .orElseThrow(() -> new MemberUnAuthorizationException("아이디와 패스워드를 다시 확인 후 로그인해주세요."));

        member.checkPassword(memberLoginRequest.password());

        //TODO: jwt 토큰 생성
    }
}
