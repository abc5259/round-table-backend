package com.roundtable.roundtable.domain.chore;

import com.roundtable.roundtable.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChoreMemberRepository extends JpaRepository<ChoreMember, Long> {
    boolean existsChoreMemberByMemberAndChore(Member member, Chore chore);

    @Query("select cm.member from ChoreMember cm join cm.member where cm.chore = :chore")
    List<Member> findMembersByChore(@Param("chore") Chore chore);
}
