package com.roundtable.roundtable.global.support.interceptor;

import static com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode.INVALID_AUTH;

import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.business.token.dto.JwtPayload;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal == null) {
            throw new AuthenticationException(INVALID_AUTH);
        }

        return true;
    }
}
