package com.roundtable.roundtable.entity.housework;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseWorkMemberRepository extends JpaRepository<HouseWorkMember, Long>, CustomHouseWorkMemberRepository {
}
