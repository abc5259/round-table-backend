package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.business.auth.AuthCode;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class EmailAuthCodeManager {

    public static final String AUTH_CODE_KEY_PREFIX = "authcode";

    public static final long AUTH_CODE_TIME_OUT = 3;

    public static final TimeUnit AUTH_CODE_TIME_UNIT = TimeUnit.MINUTES;

    private final ValueOperations<String, String> valueOperations;

    public EmailAuthCodeManager(RedisTemplate<String, String> redisTemplate) {
        valueOperations = redisTemplate.opsForValue();
    }

    public void saveAuthCode(String email, AuthCode authCode) {
        valueOperations.set(getAuthKey(email), authCode.getCode(), AUTH_CODE_TIME_OUT, AUTH_CODE_TIME_UNIT);
    }

    public boolean isCorrectAuthCode(String email, AuthCode authCode) {
        AuthCode storedAuthCode = getStoredAuthCode(email);
        return authCode.isSameCode(storedAuthCode);
    }

    private AuthCode getStoredAuthCode(String email) {
        String code = valueOperations.get(getAuthKey(email));
        return new AuthCode(code);
    }

    public String getAuthKey(String email) {
        return String.format("%s:%s", AUTH_CODE_KEY_PREFIX, email);
    }
}
