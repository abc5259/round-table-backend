package com.roundtable.roundtable.business.token;

import com.roundtable.roundtable.business.token.dto.JwtPayload;
import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.business.token.dto.CreateToken;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.token.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenManager {

    private final TokenReader tokenReader;

    private final TokenAppender tokenAppender;

    private final TokenUpdater tokenUpdater;

    private final JwtProvider jwtProvider;

    @Transactional
    public Tokens saveOrUpdateToken(Member member) {
        Tokens tokens = jwtProvider.issueToken(toJwtPayload(member));

        tokenReader.readByMemberId(member.getId())
                .ifPresentOrElse(
                        token -> tokenUpdater.updateRefreshToken(token, tokens.getRefreshToken()),
                        () -> tokenAppender.appendToken(new CreateToken(member.getId(), tokens.getRefreshToken()))
                );

        return tokens;
    }

    @Transactional
    public Tokens refresh(String refreshToken) {
        /**
         * 1. 사용자가 요청으로 준 refresh token으로 refresh token에서 Payload 추출
         * 2. 추출한 paylaod에셔 userId랑 요청으로 들어온 refreshToken으로 Token Entity 조회
         * 3. access token이랑 refresh token 새로 발급
         * 4. refresh token 갱신 (Refresh token rotation)
         * 5. 3에서 새로 발급받은 토큰들 반환
         */
        log.info(refreshToken);
        JwtPayload jwtPayload = jwtProvider.extractPayload(refreshToken);
        Token token = tokenReader.readByMemberIdAndRefreshToken(jwtPayload.userId(), refreshToken);
        Tokens tokens = jwtProvider.issueToken(jwtPayload);
        tokenUpdater.updateRefreshToken(token, tokens.getRefreshToken());

        return tokens;
    }

    private JwtPayload toJwtPayload(Member member) {
        if(member.isEnterHouse()) {
            return new JwtPayload(member.getId(), member.getHouse().getId());
        }

        return new JwtPayload(member.getId(), null);
    }


}
