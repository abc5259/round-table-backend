package com.roundtable.roundtable.entity.chore;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
}
