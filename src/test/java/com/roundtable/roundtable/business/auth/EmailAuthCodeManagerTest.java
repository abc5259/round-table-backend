package com.roundtable.roundtable.business.auth;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.otp.AuthCode;
import com.roundtable.roundtable.domain.otp.AuthCodeRedisRepository;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

class EmailAuthCodeManagerTest extends IntegrationTestSupport {

    @Autowired
    private EmailAuthCodeManager emailAuthCodeManager;

    @Autowired
    private AuthCodeRedisRepository authCodeRedisRepository;

    @AfterEach
    void tearDown() {
        authCodeRedisRepository.deleteAll();
    }

    @DisplayName("인증코드를 redis에 저장한다.")
    @Test
    void saveAuthCode() {
        //given
        String email = "email@domain.com";
        AuthCode authCode = AuthCode.of(email,"123456");

        //when
        emailAuthCodeManager.saveAuthCode(authCode);

        //then
        AuthCode findAuthCode = authCodeRedisRepository.findById(email).orElse(null);
        assertThat(findAuthCode).isNotNull();
        assertThat(findAuthCode.getCode()).isEqualTo(authCode.getCode());
    }

    @DisplayName("인증 코드가 맞는지 확인할 수 있다.")
    @Test
    void isCorrectAuthCode() {
        //given
        String email = "email@domain.com";
        AuthCode correctAuthCode = AuthCode.of(email,"123456");
        AuthCode notCorrectAuthCode = AuthCode.of(email,"456789");

        emailAuthCodeManager.saveAuthCode(correctAuthCode);

        //when
        boolean result1 = emailAuthCodeManager.isCorrectAuthCode(correctAuthCode);
        boolean result2 = emailAuthCodeManager.isCorrectAuthCode(notCorrectAuthCode);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
     }
}