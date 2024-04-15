package com.roundtable.roundtable.global.support.argumentresolver;

import static com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode.INVALID_AUTH;

import com.roundtable.roundtable.business.auth.dto.JwtPayload;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.global.support.annotation.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginAuthMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberReader memberReader;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        boolean hasAuthMemberType = AuthMember.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginAnnotation && hasAuthMemberType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal == null) {
            throw new AuthenticationException(INVALID_AUTH);
        }

        JwtPayload jwtPayload = (JwtPayload) principal;

        if(memberReader.nonExistMemberId(jwtPayload.userId())) {
            throw new AuthenticationException(INVALID_AUTH);
        }

        return new AuthMember(jwtPayload.userId(), jwtPayload.houseId());
    }
}
