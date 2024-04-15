package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.business.chore.dto.response.ChoreResponse;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.chore.dto.response.ChoreOfMemberResponse;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChoreService {

    private final ChoreReader choreReader;

    public List<ChoreOfMemberResponse> findChoresOfMember(Long memberId, LocalDate date, Long houseId) {
        return choreReader.readChoresOfMember(memberId, date, houseId);
    }

    public CursorBasedResponse<List<ChoreResponse>> findChoresOfHouse(LocalDate date, Long houseId, CursorBasedRequest cursorBasedRequest) {
        return choreReader.readChoresOfHouse(date, houseId, cursorBasedRequest);
    }
}
