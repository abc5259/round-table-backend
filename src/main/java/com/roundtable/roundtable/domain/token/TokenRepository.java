package com.roundtable.roundtable.domain.token;

import com.roundtable.roundtable.domain.member.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
    boolean existsByMember(Member member);

    Optional<Token> findByMember(Member member);

    Optional<Token> findByMemberAndRefreshToken(Member member, String refreshToken);
}
