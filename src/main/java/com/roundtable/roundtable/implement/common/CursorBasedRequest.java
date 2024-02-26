package com.roundtable.roundtable.implement.common;

import com.roundtable.roundtable.entity.common.CursorPagination;

public record CursorBasedRequest(
        Long lastId,
        Integer limit
) {
    public CursorPagination toCursorPagination() {
        return new CursorPagination(lastId,limit);
    }
}
