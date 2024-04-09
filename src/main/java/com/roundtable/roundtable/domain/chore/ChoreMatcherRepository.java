package com.roundtable.roundtable.domain.chore;

import static com.querydsl.core.types.Projections.fields;
import static com.roundtable.roundtable.domain.chore.QChore.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public class ChoreMatcherRepository {

    private final JPAQueryFactory queryFactory;

    public ChoreMatcherRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Chore> findAllByIds(List<Long> ids) {

        return queryFactory
                .select(fields(Chore.class, // 이렇게 할 경우 Entity가 아닌 Dto로 조회됨
                        chore.id,
                        chore.matchKey
                ))
                .from(chore)
                .where(chore.id.in(ids))
                .fetch();
    }
}
