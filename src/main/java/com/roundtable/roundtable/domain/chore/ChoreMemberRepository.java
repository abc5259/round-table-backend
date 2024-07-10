package com.roundtable.roundtable.domain.chore;

import com.roundtable.roundtable.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChoreMemberRepository extends JpaRepository<ChoreMember, Long> {
    boolean existsChoreMemberByMemberAndChore(Member member, Chore chore);

    List<Member> findMembersByChore(Chore chore);
}
