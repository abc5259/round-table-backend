package com.roundtable.roundtable.business.auth;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

class EmailAuthCodeManagerTest extends IntegrationTestSupport {

    @Autowired
    private EmailAuthCodeManager emailAuthCodeManager;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @AfterEach
    void tearDown() {
        Set<String> keys = redisTemplate.keys("*");
        if(Objects.nonNull(keys)) {
            redisTemplate.delete(keys);
        }
    }

    @DisplayName("인증코드를 redis에 저장한다.")
    @Test
    void saveAuthCode() {
        //given
        String email = "email@domain.com";
        AuthCode authCode = new AuthCode("123456");

        //when
        emailAuthCodeManager.saveAuthCode(email, authCode);

        //then
        String key = emailAuthCodeManager.getAuthKey(email);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        assertThat(valueOperations.get(key)).isEqualTo(authCode.getCode());
        assertThat(getExpirationInSeconds(key)).isGreaterThan(-1);
    }

    @DisplayName("인증 코드가 맞는지 확인할 수 있다.")
    @Test
    void isCorrectAuthCode() {
        //given
        String email = "email@domain.com";
        AuthCode correctAuthCode = new AuthCode("123456");
        AuthCode notCorrectAuthCode = new AuthCode("323456");

        emailAuthCodeManager.saveAuthCode(email, correctAuthCode);

        //when
        boolean result1 = emailAuthCodeManager.isCorrectAuthCode(email, correctAuthCode);
        boolean result2 = emailAuthCodeManager.isCorrectAuthCode(email, notCorrectAuthCode);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
     }


    private Long getExpirationInSeconds(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }
}