package com.template.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAuthorityEntity is a Querydsl query type for AuthorityEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorityEntity extends EntityPathBase<AuthorityEntity> {

    private static final long serialVersionUID = 1258031419L;

    public static final QAuthorityEntity authorityEntity = new QAuthorityEntity("authorityEntity");

    public final com.template.global.common.QBaseEntity _super = new com.template.global.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final EnumPath<com.template.global.common.enums.Role> role = createEnum("role", com.template.global.common.enums.Role.class);

    public final ListPath<UserAuthorityEntity, QUserAuthorityEntity> userAuthorityEntityList = this.<UserAuthorityEntity, QUserAuthorityEntity>createList("userAuthorityEntityList", UserAuthorityEntity.class, QUserAuthorityEntity.class, PathInits.DIRECT2);

    public QAuthorityEntity(String variable) {
        super(AuthorityEntity.class, forVariable(variable));
    }

    public QAuthorityEntity(Path<? extends AuthorityEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthorityEntity(PathMetadata metadata) {
        super(AuthorityEntity.class, metadata);
    }

}

