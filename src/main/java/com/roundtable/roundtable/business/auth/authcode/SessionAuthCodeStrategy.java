package com.roundtable.roundtable.business.auth.authcode;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionAuthCodeStrategy implements AuthCodeStoreStrategy {

    private final HttpServletRequest request;

    @Override
    public void saveAuthCode(AuthCode authCode) {
        HttpSession session = request.getSession();
        session.setAttribute(AUTH_CODE_STORE_KEY, authCode.getCode());
    }

    @Override
    public boolean isCorrectAuthCode(AuthCode authCode) {
        HttpSession session = request.getSession(false);
        if(session == null) {
            return false;
        }

        String correctAuthCode = (String) session.getAttribute(AUTH_CODE_STORE_KEY);
        return correctAuthCode.equals(authCode.getCode());
    }
}
