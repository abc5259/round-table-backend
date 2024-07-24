package com.roundtable.roundtable.business.common;

import com.roundtable.roundtable.domain.common.CursorPagination;

public record CursorBasedRequest(
        Long lastId,
        Integer limit
) {
    public static final Long DEFAULT_LAST_ID = 0L;

    public CursorPagination toCursorPagination() {
        return new CursorPagination(lastId,limit);
    }
}
