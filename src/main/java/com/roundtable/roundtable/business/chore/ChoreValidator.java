package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.global.exception.ChoreException.AlreadyCompletedException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChoreValidator {


    private final ChoreMemberReader choreMemberReader;


    public void validateChoreAssignedToMember(Long choreId, Long memberId) {
        boolean exist = choreMemberReader.existByMemberIdAndChoreId(choreId, memberId);
        if (!exist) {
            throw new NotFoundEntityException();
        }
    }

    public void validateCompleteChore(Chore chore) {
        if(chore.isCompleted()) {
            throw new AlreadyCompletedException();
        }
    }
}
