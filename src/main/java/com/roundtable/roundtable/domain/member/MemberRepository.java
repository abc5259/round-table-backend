package com.roundtable.roundtable.domain.member;

import com.roundtable.roundtable.domain.house.House;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    boolean existsByIdAndHouse(Long id, House house);
}
