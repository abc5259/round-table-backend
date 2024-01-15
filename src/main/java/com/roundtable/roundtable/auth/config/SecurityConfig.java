package com.roundtable.roundtable.auth.config;

import com.roundtable.roundtable.auth.jwt.filter.JwtAuthenticationConverter;
import com.roundtable.roundtable.auth.jwt.filter.JwtAuthenticationFilter;
import com.roundtable.roundtable.auth.jwt.provider.JwtAuthenticationProvider;
import com.roundtable.roundtable.auth.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] whiteList = {"/members/register", "/members/login", "/members/emails/verification-requests"};

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(whiteList);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.anyRequest().authenticated())
                .addFilterBefore(new JwtAuthenticationFilter(jwtAuthenticationConverter, jwtAuthenticationProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
