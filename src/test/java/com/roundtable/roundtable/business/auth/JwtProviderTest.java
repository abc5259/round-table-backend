package com.roundtable.roundtable.business.auth;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.business.auth.dto.JwtPayload;
import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode;
import com.roundtable.roundtable.global.properties.JwtProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @DisplayName("Token을 발행할 수 있다.")
    @Test
    void issueToken() {
        //given
        JwtProvider jwtProvider = createJwtProvider();

        //when
        Tokens tokens = jwtProvider.issueToken(new JwtPayload(1L, 1L));

        //then
        assertThat(tokens.getAccessToken()).isNotNull();
        assertThat(tokens.getRefreshToken()).isNotNull();
        assertNotEquals(tokens.getAccessToken(), tokens.getRefreshToken());
     }

     @DisplayName("jwt의 payload를 추출할 수 있다.")
     @Test
     void extractPayload() {
         //given
         JwtProvider jwtProvider = createJwtProvider();
         JwtPayload jwtPayload = new JwtPayload(1L, 1L);
         Tokens tokens = jwtProvider.issueToken(jwtPayload);

         //when
         JwtPayload result1 = jwtProvider.extractPayload(tokens.getAccessToken());
         JwtPayload result2 = jwtProvider.extractPayload(tokens.getRefreshToken());

         //then
         assertThat(result1).isEqualTo(jwtPayload);
         assertThat(result2).isEqualTo(jwtPayload);
      }

      @DisplayName("잘못된 jwt의 payload를 추출할때는 에러를 던진다.")
      @ParameterizedTest
      @ValueSource(strings = {"", "fake token"})
      void extractPayload_fail(String token) {
          //given
          JwtProvider jwtProvider = createJwtProvider();

          //when //then
          assertThatThrownBy(() -> jwtProvider.extractPayload(token))
                  .isInstanceOf(AuthenticationException.class)
                  .hasMessage(AuthErrorCode.JWT_EXTRACT_ERROR.getMessage());
       }

    @DisplayName("유효기간이 지난 jwt의 payload를 추출할때는 에러를 던진다.")
    @Test
    void extractPayload_expireToken() {
        //given
        JwtProperties jwtProperties = createJwtProperties(0L, 0L);
        JwtProvider jwtProvider = new JwtProvider(jwtProperties);
        Tokens tokens = jwtProvider.issueToken(new JwtPayload(10L, 10L));

        //when //then
        assertThatThrownBy(() -> jwtProvider.extractPayload(tokens.getAccessToken()))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage(AuthErrorCode.JWT_EXPIRED_ERROR.getMessage());
    }

    private JwtProvider createJwtProvider() {
        JwtProperties jwtProperties = createJwtProperties(100000L, 200000L);
        return new JwtProvider(jwtProperties);
    }

    private JwtProperties createJwtProperties(long accessTokenExpireTime, long refreshTokenExpireTime) {
        String secretKey = createString(256);
        return new JwtProperties(secretKey, accessTokenExpireTime, refreshTokenExpireTime);
    }

    public String createString(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append("A".repeat(Math.max(0, length)));
        return sb.toString();
     }


}