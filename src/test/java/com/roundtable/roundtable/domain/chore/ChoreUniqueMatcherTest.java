package com.roundtable.roundtable.domain.chore;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChoreUniqueMatcherTest {
    @DisplayName("Chore마다 유니크 키가 생성된다.")
    @Test
    void ChoreUniqueMatcher() {
        //given
        int size = 1_000;
        List<Chore> chores = IntStream.rangeClosed(1, size)
                .mapToObj(i -> Chore.builder().build())
                .toList();

        //when
        ChoreUniqueMatcher choreUniqueMatcher = new ChoreUniqueMatcher(chores);

        //then
        assertThat(choreUniqueMatcher.getMap()).hasSize(size);
        assertThat(choreUniqueMatcher.getChores().get(0)).isNotNull();
    }

    @DisplayName("Chore ID로 ChoreMember를 찾는다")
    @Test
    void getChoreMembers() {
        //given
        int size = 1_000;
        List<Chore> chores = IntStream.rangeClosed(1, size)
                .mapToObj(i -> Chore.builder().build())
                .peek(c -> c.addChoreMembers(List.of(ChoreMember.builder().build())))
                .toList();


        //when
        ChoreUniqueMatcher choreUniqueMatcher = new ChoreUniqueMatcher(chores);

        //then
        for(Chore idChore: chores) {
            List<ChoreMember> choreMembers = choreUniqueMatcher.getChoreMembers(idChore);
            assertThat(choreMembers).hasSize(1);

            ChoreMember choreMember = choreMembers.get(0);
            assertThat(choreMember.getChore()).isEqualTo(idChore);
        }

    }
}