package com.roundtable.roundtable.presentation.auth.jwt;

import com.roundtable.roundtable.business.auth.Tokens;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Tokens tokens;
    private final Object principal;
    private final Object credentials;

    public JwtAuthenticationToken(Tokens tokens, Object principal, Object credentials) {
        super(null);
        this.tokens = tokens;
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(false);
    }

    public JwtAuthenticationToken(Tokens tokens, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.tokens = tokens;
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public Tokens getTokens() {
        return this.tokens;
    }

}
