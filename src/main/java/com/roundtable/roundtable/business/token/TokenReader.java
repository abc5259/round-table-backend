package com.roundtable.roundtable.business.token;

import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.token.Token;
import com.roundtable.roundtable.domain.token.TokenRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenReader {

    private final TokenRepository tokenRepository;

    public Optional<Token> readByMemberId(Long memberId) {
        return tokenRepository.findByMember(Member.Id(memberId));
    }
}
