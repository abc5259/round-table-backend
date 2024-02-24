package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoreReader {
    private final ChoreRepository choreRepository;

//    public List<Chore> findAllChoresByDate(LocalDate date) {
//
//    }
}
