package com.roundtable.roundtable.domain.delegation;

import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DelegationRepository extends JpaRepository<Delegation, Long> {
    boolean existsByScheduleIdAndSenderIdAndDelegationDate(Long scheduleId, Long senderId, LocalDate delegationDate);
}
