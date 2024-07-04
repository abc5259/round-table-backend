package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.domain.chore.Chore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChoreUpdater {

    private final ChoreReader choreReader;

    public void completeChore(Long choreId) {

    }
}
