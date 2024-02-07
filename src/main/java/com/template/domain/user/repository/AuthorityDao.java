package com.template.domain.user.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.template.domain.user.entity.AuthorityEntity;
import com.template.global.common.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.template.domain.user.entity.QAuthorityEntity.authorityEntity;

@Repository
@RequiredArgsConstructor
public class AuthorityDao {
    private final JPAQueryFactory queryFactory;


    public AuthorityEntity findAuthorityEntityByRole(Role role) {
        return queryFactory
                        .selectFrom(authorityEntity)
                        .where(authorityEntity.role.eq(role))
                        .fetchFirst();
    }

    public List<AuthorityEntity> findAuthorityEntityListByRoleList(List<Role> roleList) {
        return queryFactory
                .selectFrom(authorityEntity)
                .where(authorityEntity.role.in(roleList))
                .fetch();
    }

}

