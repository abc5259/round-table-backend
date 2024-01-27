package com.roundtable.roundtable.house.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHouse is a Querydsl query type for House
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHouse extends EntityPathBase<House> {

    private static final long serialVersionUID = -786214287L;

    public static final QHouse house = new QHouse("house");

    public final com.roundtable.roundtable.global.domain.QBaseEntity _super = new com.roundtable.roundtable.global.domain.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.roundtable.roundtable.member.domain.Member, com.roundtable.roundtable.member.domain.QMember> members = this.<com.roundtable.roundtable.member.domain.Member, com.roundtable.roundtable.member.domain.QMember>createList("members", com.roundtable.roundtable.member.domain.Member.class, com.roundtable.roundtable.member.domain.QMember.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QHouse(String variable) {
        super(House.class, forVariable(variable));
    }

    public QHouse(Path<? extends House> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHouse(PathMetadata metadata) {
        super(House.class, metadata);
    }

}

