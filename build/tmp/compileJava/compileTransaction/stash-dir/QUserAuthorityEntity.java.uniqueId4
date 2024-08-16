package com.template.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserAuthorityEntity is a Querydsl query type for UserAuthorityEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAuthorityEntity extends EntityPathBase<UserAuthorityEntity> {

    private static final long serialVersionUID = 685917040L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserAuthorityEntity userAuthorityEntity = new QUserAuthorityEntity("userAuthorityEntity");

    public final com.template.global.common.QBaseEntity _super = new com.template.global.common.QBaseEntity(this);

    public final QAuthorityEntity authorityEntity;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final QUserEntity userEntity;

    public QUserAuthorityEntity(String variable) {
        this(UserAuthorityEntity.class, forVariable(variable), INITS);
    }

    public QUserAuthorityEntity(Path<? extends UserAuthorityEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserAuthorityEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserAuthorityEntity(PathMetadata metadata, PathInits inits) {
        this(UserAuthorityEntity.class, metadata, inits);
    }

    public QUserAuthorityEntity(Class<? extends UserAuthorityEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.authorityEntity = inits.isInitialized("authorityEntity") ? new QAuthorityEntity(forProperty("authorityEntity")) : null;
        this.userEntity = inits.isInitialized("userEntity") ? new QUserEntity(forProperty("userEntity")) : null;
    }

}

