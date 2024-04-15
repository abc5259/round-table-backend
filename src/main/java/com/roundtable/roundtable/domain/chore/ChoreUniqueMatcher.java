package com.roundtable.roundtable.domain.chore;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ChoreUniqueMatcher {

    private final Map<String, Chore> map = new LinkedHashMap<>();

    public ChoreUniqueMatcher(List<Chore> items) {
        for(Chore chore: items) {
            String matchKey = UUID.randomUUID().toString();
            chore.setMatchKey(matchKey);
            map.put(matchKey, chore);
        }
    }

    public List<Chore> getChores() {
        return new ArrayList<>(map.values());
    }

    public List<ChoreMember> getChoreMembers(Chore idChore) {
        Chore chore = map.get(idChore.getMatchKey());

        if(chore == null) {
            return new ArrayList<>();
        }

        return chore.getChoreMembers().stream()
                .map(choreMember -> choreMember.setBulkInsert(idChore))
                .toList();
    }
}
