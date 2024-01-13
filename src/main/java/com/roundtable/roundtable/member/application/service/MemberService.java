package com.roundtable.roundtable.member.application.service;

import com.roundtable.roundtable.auth.jwt.provider.JwtProvider;
import com.roundtable.roundtable.auth.jwt.provider.Token;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

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
        this.checkDuplicatedEmail(memberRegisterRequest.email());

        Member member = Member.of(memberRegisterRequest.email(), memberRegisterRequest.password(), passwordEncoder);
        memberRepository.save(member);

        log.info(String.format("%s 이메일을 가진 User 생성", memberRegisterRequest.email()));
    }

    @Transactional(readOnly = true)
    public Token login(final MemberLoginRequest memberLoginRequest) {
        Member member = memberRepository.findByEmail(memberLoginRequest.email())
                .orElseThrow(() -> new MemberUnAuthorizationException("아이디와 패스워드를 다시 확인 후 로그인해주세요."));

        member.checkPassword(memberLoginRequest.password(), passwordEncoder);

        //TODO: jwt 토큰 생성
        return jwtProvider.issueToken(member.getId());
    }
}
