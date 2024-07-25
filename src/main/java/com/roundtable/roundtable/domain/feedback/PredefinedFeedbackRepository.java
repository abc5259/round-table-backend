package com.roundtable.roundtable.domain.feedback;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredefinedFeedbackRepository extends JpaRepository<PredefinedFeedback, Long> {
    List<PredefinedFeedback> findByIdIn(List<Long> id);
}
