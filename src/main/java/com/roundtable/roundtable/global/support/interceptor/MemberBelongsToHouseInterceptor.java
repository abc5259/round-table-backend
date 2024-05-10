package com.roundtable.roundtable.global.support.interceptor;

import static com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode.INVALID_AUTH;

import com.roundtable.roundtable.business.token.dto.JwtPayload;
import com.roundtable.roundtable.business.member.MemberValidator;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.global.support.annotation.ValidHasHouse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Component
@RequiredArgsConstructor
public class MemberBelongsToHouseInterceptor implements HandlerInterceptor {

    private static final String PATH_VARIABLE_KEY = "houseId";

    private final MemberValidator memberValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if (Objects.isNull(pathVariables.get(PATH_VARIABLE_KEY))) {
            return true;
        }

        final Long houseId = Long.parseLong(pathVariables.get(PATH_VARIABLE_KEY));

        memberValidator.validateMemberBelongsToHouse(houseId);

        return true;
    }
}
