package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.business.auth.dto.JwtPayload;
import com.roundtable.roundtable.business.auth.dto.Tokens;
import com.roundtable.roundtable.global.exception.AuthenticationException;
import com.roundtable.roundtable.global.exception.errorcode.AuthErrorCode;
import com.roundtable.roundtable.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private static final String USER_ID = "userId";

    private static final String HOUSE_ID = "houseId";

    public Tokens issueToken(JwtPayload jwtPayload) {
        return Tokens.of(
                generateToken(jwtPayload.userId(), jwtPayload.houseId(),jwtProperties.getAccessTokenExpireTime()),
                generateToken(jwtPayload.userId(), jwtPayload.houseId(),jwtProperties.getRefreshTokenExpireTime())
        );
    }

    private String generateToken(Long userId, Long houseId, Long expireTime) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + expireTime);
        return Jwts.builder()
                .claim(USER_ID, userId)
                .claim(HOUSE_ID, houseId)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(jwtProperties.getSecretKey(), SIG.HS256)
                .compact();
    }

    public JwtPayload extractPayload(String token) {
        Claims claims = extractClaims(token);
        Long userId = claims.get(USER_ID, Long.class);
        Long houseId = claims.get(HOUSE_ID, Long.class);
        return new JwtPayload(userId, houseId);
    }

    private Claims extractClaims(String token) {
        try {
            return getJwtParser().parseSignedClaims(token).getPayload();
        }
        catch (ExpiredJwtException e) {
            throw new AuthenticationException(AuthErrorCode.JWT_EXPIRED_ERROR, e);
        }
        catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException(AuthErrorCode.JWT_EXTRACT_ERROR, e);
        }
    }

    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(jwtProperties.getSecretKey()).build();
    }
}
