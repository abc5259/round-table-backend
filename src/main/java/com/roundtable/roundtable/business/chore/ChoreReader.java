package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.domain.chore.Chore;
import com.roundtable.roundtable.domain.chore.ChoreQueryRepository;
import com.roundtable.roundtable.domain.chore.ChoreRepository;
import com.roundtable.roundtable.domain.chore.dto.ChoreMembersDetailDto;
import com.roundtable.roundtable.business.chore.dto.response.ChoreOfMemberResponse;
import com.roundtable.roundtable.business.chore.dto.response.ChoreResponse;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
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
    private final ChoreQueryRepository choreQueryRepository;

    public List<ChoreOfMemberResponse> readChoresOfMember(Long memberId, LocalDate date, Long houseId) {
        return choreQueryRepository.findChoresOfMember(memberId, date, houseId)
                .stream().map(ChoreOfMemberResponse::create).toList();
    }

    public CursorBasedResponse<List<ChoreResponse>> readChoresOfHouse(LocalDate date, Long houseId, CursorBasedRequest cursorBasedRequest) {
        List<ChoreMembersDetailDto> choresOfHouse = choreQueryRepository.findChoresOfHouse(date, houseId, cursorBasedRequest.toCursorPagination());

        List<ChoreResponse> choreResponses = choresOfHouse.stream().map(ChoreResponse::create).toList();
        Long lastChoreId = choresOfHouse.isEmpty() ? 0 : choresOfHouse.get(choresOfHouse.size() - 1).choreId();

        return CursorBasedResponse.of(choreResponses, lastChoreId);
    }

    public Chore readById(Long choreId) {
        Chore chore = choreRepository.findById(choreId).orElseThrow(NotFoundEntityException::new);
        return chore;
    }
}
