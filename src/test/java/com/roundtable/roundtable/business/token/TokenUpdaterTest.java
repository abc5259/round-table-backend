package com.roundtable.roundtable.business.token;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.token.Token;
import com.roundtable.roundtable.domain.token.TokenRepository;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class TokenUpdaterTest extends IntegrationTestSupport {

    @Autowired
    private TokenUpdater tokenUpdater;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Token의 refreshToken을 업데이트할 수 있다.")
    @Test
    void updateRefreshToken() {
        //given
        Member member = createMember();
        Token token = createToken(member, "refreshToken");
        String newRefreshToken = "newRefreshToken";

        //when
        tokenUpdater.updateRefreshToken(token, newRefreshToken);

        //then
        List<Token> tokens = tokenRepository.findAll();
        assertThat(tokens).hasSize(1);
        Token findToken = tokens.get(0);
        assertThat(findToken.getRefreshToken()).isEqualTo(newRefreshToken);
    }

    private Member createMember() {
        Member member = Member.builder()
                .email("email")
                .password("password")
                .build();
        return memberRepository.save(member);
    }

    private Token createToken(Member member, String refreshToken) {
        Token token = Token.builder().member(member).refreshToken(refreshToken).build();
        return tokenRepository.save(token);
    }
}