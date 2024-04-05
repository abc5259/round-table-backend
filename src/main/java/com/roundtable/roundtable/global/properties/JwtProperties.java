package com.roundtable.roundtable.global.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Base64;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank
    private final String secretKey;

    @NotEmpty
    private final Long accessTokenExpireTime;

    @NotEmpty
    private final Long refreshTokenExpireTime;

    public JwtProperties(String secretKey, Long accessTokenExpireTime, Long refreshTokenExpireTime) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }
}
