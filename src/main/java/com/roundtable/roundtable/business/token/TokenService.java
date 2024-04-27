package com.roundtable.roundtable.business.token;

import com.roundtable.roundtable.business.auth.dto.Tokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenManager tokenManager;

    public Tokens refresh(String refreshToken) {
        return tokenManager.refresh(refreshToken);
    }
}
