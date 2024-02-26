package com.roundtable.roundtable.implement.chore;

import com.roundtable.roundtable.entity.chore.ChoreQueryRepository;
import com.roundtable.roundtable.entity.chore.dto.ChoreDetailV1Dto;
import com.roundtable.roundtable.entity.chore.dto.ChoreMembersDetailDto;
import com.roundtable.roundtable.entity.common.CursorPagination;
import com.roundtable.roundtable.implement.common.CursorBasedResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChoreReader {
    private final ChoreQueryRepository choreQueryRepository;

    public List<ChoreDetailV1Dto> readByIdAndDateInHouse(Long memberId, LocalDate date, Long houseId) {
        return choreQueryRepository.findByIdAndDateInHouse(memberId, date, houseId);
    }

    public CursorBasedResponse<List<ChoreResponse>> readChoreMembersByDateSinceLastChoreIdInHouse(LocalDate date, Long houseId, CursorPagination cursorPagination) {
        List<ChoreMembersDetailDto> choreMembersDetail = choreQueryRepository.findChoreMembersByDateSinceLastChoreIdInHouse(
                date, houseId, cursorPagination);

        List<ChoreResponse> choreResponses = choreMembersDetail.stream().map(ChoreResponse::create).toList();
        Long lastChoreId = choreMembersDetail.get(choreMembersDetail.size() - 1).choreId();

        return CursorBasedResponse.of(choreResponses, lastChoreId);
    }
}
