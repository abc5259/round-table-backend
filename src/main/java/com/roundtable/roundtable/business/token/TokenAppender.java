package com.roundtable.roundtable.business.token;

import com.roundtable.roundtable.domain.token.Token;
import com.roundtable.roundtable.domain.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class TokenAppender {

    private final TokenRepository tokenRepository;

    public Long appendToken(CreateToken createToken) {
        Token token = Token.of(createToken.memberId(), createToken.refreshToken());
        tokenRepository.save(token);
        return token.getId();
    }
}
