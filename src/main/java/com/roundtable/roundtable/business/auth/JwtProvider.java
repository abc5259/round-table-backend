package com.roundtable.roundtable.business.auth;

import com.roundtable.roundtable.global.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    private static final String USER_ID = "userId";
    private static final String HOUSE_ID = "houseId";

    public Token issueToken(JwtPayload jwtPayload) {
        return Token.of(
                generateToken(jwtPayload.userId(), jwtPayload.houseId(),true),
                generateToken(jwtPayload.userId(), jwtPayload.houseId(),false)
        );
    }

    private String generateToken(Long userId, Long houseId, boolean isAccessToken) {
        final Date now = new Date();
        final Date expiration = new Date(now.getTime() + (isAccessToken ? jwtProperties.getAccessTokenExpireTime() : jwtProperties.getRefreshTokenExpireTime()));
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim(USER_ID,userId)
                .claim(HOUSE_ID, houseId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getJwtParser().parseClaimsJws(token).getBody();
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public JwtPayload getSubject(String token) {
        Claims claims = getJwtParser().parseClaimsJws(token).getBody();
        Long userId = claims.get(USER_ID, Long.class);
        Long houseId = claims.get(HOUSE_ID, Long.class);
        return new JwtPayload(userId, houseId);
    }

    private JwtParser getJwtParser() {
        return Jwts.parser().setSigningKey(jwtProperties.getSecretKey());
    }
}
