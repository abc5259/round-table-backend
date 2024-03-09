package com.roundtable.roundtable.presentation.common.request;

import com.roundtable.roundtable.business.common.CursorBasedRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.Objects;

public record CursorBasedPaginationRequest(

        Long lastId,
        @Min(1) @Max(30)
        Integer limit
) {
    public final static Long DEFAULT_LAST_ID = 0L;
    public final static Integer DEFAULT_LIMIT = 20;

    public CursorBasedRequest toCursorBasedRequest() {
        return new CursorBasedRequest(
                Objects.isNull(lastId) ? DEFAULT_LAST_ID : lastId,
                Objects.isNull(limit) ? DEFAULT_LIMIT : limit
        );
    }
}
