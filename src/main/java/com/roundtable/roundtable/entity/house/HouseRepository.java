package com.roundtable.roundtable.entity.house;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {

    boolean existsByInviteCode(InviteCode inviteCode);
}
