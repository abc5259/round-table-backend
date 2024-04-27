package com.roundtable.roundtable.business.token;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.token.dto.CreateToken;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.token.Token;
import com.roundtable.roundtable.domain.token.TokenRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class TokenAppenderTest extends IntegrationTestSupport {

    @Autowired
    private TokenAppender tokenAppender;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("토큰을 저장한다.")
    @Test
    void appendToken() {
        //given
        Member member = createMember();
        CreateToken createToken = new CreateToken(member.getId(), "token");

        //when
        tokenAppender.appendToken(createToken);

        //then
        List<Token> tokens = tokenRepository.findAll();
        assertThat(tokens).hasSize(1);
        Token token = tokens.get(0);
        assertThat(token.getRefreshToken()).isEqualTo(createToken.refreshToken());
        assertThat(token.getMember().getId()).isEqualTo(member.getId());
    }


    public Member createMember() {
        Member member = Member.builder()
                .email("email")
                .password("password")
                .build();
        return memberRepository.save(member);
    }


}