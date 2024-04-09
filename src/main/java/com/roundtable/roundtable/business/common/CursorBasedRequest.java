package com.roundtable.roundtable.business.common;

import com.roundtable.roundtable.domain.common.CursorPagination;

public record CursorBasedRequest(
        Long lastId,
        Integer limit
) {
    public CursorPagination toCursorPagination() {
        return new CursorPagination(lastId,limit);
    }
}
