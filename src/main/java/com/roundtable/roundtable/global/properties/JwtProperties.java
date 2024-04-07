package com.roundtable.roundtable.global.properties;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank
    private final SecretKey secretKey;

    @NotEmpty
    private final Long accessTokenExpireTime;

    @NotEmpty
    private final Long refreshTokenExpireTime;

    public JwtProperties(String secretKey, Long accessTokenExpireTime, Long refreshTokenExpireTime) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }
}
