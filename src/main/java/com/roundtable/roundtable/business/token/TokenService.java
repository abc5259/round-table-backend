package com.roundtable.roundtable.business.token;

import com.roundtable.roundtable.business.token.dto.CreateToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenReader tokenReader;

    private final TokenAppender tokenAppender;

    private final TokenUpdater tokenUpdater;

    @Transactional
    public void saveOrUpdateToken(CreateToken createToken) {
        tokenReader.readByMemberId(createToken.memberId())
                .ifPresentOrElse(
                        token -> tokenUpdater.updateRefreshToken(token, createToken.refreshToken()),
                        () -> tokenAppender.appendToken(createToken)
                );
    }
}
