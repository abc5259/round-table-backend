package com.roundtable.roundtable.business.chore;

import com.roundtable.roundtable.business.chore.event.ChoreCompleteEvent;
import com.roundtable.roundtable.business.chore.dto.response.ChoreResponse;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.common.CursorBasedRequest;
import com.roundtable.roundtable.business.chore.dto.response.ChoreOfMemberResponse;
import com.roundtable.roundtable.business.common.CursorBasedResponse;
import com.roundtable.roundtable.business.image.ImageUploader;
import com.roundtable.roundtable.domain.chore.Chore;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChoreService {

    private final ChoreReader choreReader;

    private final ChoreValidator choreValidator;

    private final ImageUploader imageUploader;

    private final ApplicationEventPublisher eventPublisher;

    public List<ChoreOfMemberResponse> findChoresOfMember(AuthMember authMember, LocalDate date) {
        return choreReader.readChoresOfMember(authMember.memberId(), date, authMember.houseId());
    }

    public CursorBasedResponse<List<ChoreResponse>> findChoresOfHouse(LocalDate date, Long houseId, CursorBasedRequest cursorBasedRequest) {
        return choreReader.readChoresOfHouse(date, houseId, cursorBasedRequest);
    }

    @Transactional
    public void completeChore(
            AuthMember authMember,
            Long choreId,
            MultipartFile completedImage) {
        choreValidator.validateChoreAssignedToMember(choreId, authMember.memberId());

        Chore chore = choreReader.readById(choreId);
        choreValidator.validateCompleteChore(chore);

        String imageUrl = imageUploader.upload(completedImage);
        chore.complete(imageUrl);

        eventPublisher.publishEvent(new ChoreCompleteEvent(authMember.houseId(), choreId, authMember.memberId()));
    }
}
