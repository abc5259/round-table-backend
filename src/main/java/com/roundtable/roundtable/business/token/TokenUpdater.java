package com.roundtable.roundtable.business.token;

import com.roundtable.roundtable.domain.token.Token;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TokenUpdater {

    public void updateRefreshToken(Token token, String newRefreshToken) {
        token.changeRefreshToken(newRefreshToken);
    }
}
