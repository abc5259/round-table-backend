package com.roundtable.roundtable.business.auth.authcode;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

@RequiredArgsConstructor
public class RedisAuthCodeStrategy implements AuthCodeStoreStrategy{

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveAuthCode(AuthCode authCode) {
//        redisTemplate.op
    }

    @Override
    public boolean isCorrectAuthCode(AuthCode authCode) {
        return false;
    }
}
