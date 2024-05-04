package com.roundtable.roundtable.business.house.dto;

import java.util.List;

public record CreateHouse(
        String name,
        List<String> inviteEmails
) {
}
