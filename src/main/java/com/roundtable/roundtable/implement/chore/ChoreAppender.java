package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.chore.ChoreMemberAppender;
import com.roundtable.roundtable.entity.chore.ChoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class ChoreAppender {

    private final ChoreMemberAppender choreMemberAppender;
    private final ChoreRepository choreRepository;

    public Chore appendChore(CreateChore createChore) {

        Chore chore = Chore.create(createChore.schedule());
        Chore savedChore = choreRepository.save(chore);

        choreMemberAppender.createChoreMembers(savedChore, createChore.assignedMembers());

        return savedChore;
    }
}
