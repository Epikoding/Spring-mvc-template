package com.template.domain.user.repository.expression;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.template.domain.user.entity.QUserEntity.userEntity;


public class UserExpression {
    public static BooleanExpression between(LocalDateTime from, LocalDateTime to) {
        return Objects.isNull(from) || Objects.isNull(to) ? null : userEntity.createdAt.between(from, to);
    }

    public static BooleanExpression eqEmailAddress(String emailAddress) {
        return Objects.isNull(emailAddress) ? null : userEntity.email.eq(emailAddress);
    }

    public static BooleanExpression eqId(Long id) {
        return Objects.isNull(id) ? null : userEntity.id.eq(id);
    }
}
