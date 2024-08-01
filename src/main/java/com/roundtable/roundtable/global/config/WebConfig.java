package com.roundtable.roundtable.global.config;

import com.roundtable.roundtable.global.support.argumentresolver.LoginAuthMemberArgumentResolver;
import com.roundtable.roundtable.global.support.interceptor.LoginCheckInterceptor;
import com.roundtable.roundtable.global.support.interceptor.MemberBelongsToHouseInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final LoginAuthMemberArgumentResolver loginAuthMemberArgumentResolver;

    private final LoginCheckInterceptor loginCheckInterceptor;

    private final MemberBelongsToHouseInterceptor memberBelongsToHouseInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginAuthMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/members/exist")
                .excludePathPatterns("/auth/register")
                .excludePathPatterns("/auth/login")
                .excludePathPatterns("/auth/emails")
                .excludePathPatterns("/token/refresh");

        registry.addInterceptor(memberBelongsToHouseInterceptor)
                .order(2)
                .addPathPatterns("/**/house/{houseId}")
                .addPathPatterns("/**/houses/{houseId}");
    }
}
