package com.template.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 765950521L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final com.template.global.common.QBaseEntity _super = new com.template.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final StringPath email = createString("email");

    public final BooleanPath enabled = createBoolean("enabled");

    public final NumberPath<Integer> failedAttempt = createNumber("failedAttempt", Integer.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final BooleanPath locked = createBoolean("locked");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final DateTimePath<java.time.LocalDateTime> passwordUpdatedAt = createDateTime("passwordUpdatedAt", java.time.LocalDateTime.class);

    public final StringPath phone = createString("phone");

    public final ListPath<UserAuthorityEntity, QUserAuthorityEntity> userAuthorityList = this.<UserAuthorityEntity, QUserAuthorityEntity>createList("userAuthorityList", UserAuthorityEntity.class, QUserAuthorityEntity.class, PathInits.DIRECT2);

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

